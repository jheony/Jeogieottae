import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ReservationLoader {

    private static final String DB_HOST = System.getenv("DB_HOST");
    private static final String DB_PORT = System.getenv("DB_PORT");
    private static final String DB_NAME = System.getenv("DB_NAME");
    private static final String DB_USER = System.getenv("DB_USERNAME");
    private static final String DB_PASSWORD = System.getenv("DB_PASSWORD");
    private static final String JDBC_URL =
            "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME +
                    "?rewriteBatchedStatements=true" +
                    "&useServerPrepStmts=true" +
                    "&cachePrepStmts=true" +
                    "&prepStmtCacheSize=256" +
                    "&prepStmtCacheSqlLimit=2048";
    private static final int TOTAL_COUNT = 5_000_000;
    private static final int BATCH_SIZE = 2_000;
    private static final int COUPON_APPLY_RATE = 30;
    private static final String[] STATUSES = {
            "RESERVED",
            "VISITED",
            "CANCEL"
    };

    enum DiscountType { RATE, FIXED }

    static class Coupon {
        String name;
        DiscountType type;
        long value;
    }

    public static void main(String[] args) throws Exception {

        validateEnv();
        Class.forName("com.mysql.cj.jdbc.Driver");

        try (Connection conn = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD)) {
            conn.setAutoCommit(false);

            List<Long> roomIds = loadIds(conn, "rooms");
            List<Long> userIds = loadIds(conn, "users");
            List<Coupon> coupons = loadCoupons(conn);

            if (roomIds.isEmpty() || userIds.isEmpty()) {
                throw new IllegalStateException("rooms 또는 users 테이블이 비어있음");
            }

            String sql = """
                INSERT INTO reservations (
                    check_in,
                    check_out,
                    user_id,
                    room_id,
                    coupon_name,
                    guest_count,
                    original_price,
                    discounted_price,
                    status,
                    created_at,
                    is_deleted
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), false)
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                Random random = new Random();
                LocalDateTime base = LocalDateTime.of(2025, 1, 1, 14, 0);

                for (int i = 1; i <= TOTAL_COUNT; i++) {

                    long roomId = roomIds.get(random.nextInt(roomIds.size()));
                    long userId = userIds.get(random.nextInt(userIds.size()));

                    LocalDateTime checkIn =
                            base.plusDays(random.nextInt(180))
                                    .plusHours(random.nextInt(6));

                    LocalDateTime checkOut =
                            checkIn.plusDays(1 + random.nextInt(5));

                    long guestCount = 1 + random.nextInt(4);
                    long originalPrice = 50_000 + random.nextInt(200_000);

                    String couponName = null;
                    long discountedPrice = originalPrice;

                    if (!coupons.isEmpty() && random.nextInt(100) < COUPON_APPLY_RATE) {
                        Coupon coupon = coupons.get(random.nextInt(coupons.size()));
                        couponName = coupon.name;
                        discountedPrice = applyDiscount(originalPrice, coupon);
                    }

                    String status = STATUSES[random.nextInt(STATUSES.length)];

                    ps.setTimestamp(1, Timestamp.valueOf(checkIn));
                    ps.setTimestamp(2, Timestamp.valueOf(checkOut));
                    ps.setLong(3, userId);
                    ps.setLong(4, roomId);
                    ps.setString(5, couponName);
                    ps.setLong(6, guestCount);
                    ps.setLong(7, originalPrice);
                    ps.setLong(8, discountedPrice);
                    ps.setString(9, status);

                    ps.addBatch();

                    if (i % BATCH_SIZE == 0) {
                        ps.executeBatch();
                        ps.clearBatch();
                        conn.commit();
                    }

                    if (i % 100_000 == 0) {
                        System.out.println("Inserted: " + i);
                    }
                }

                ps.executeBatch();
                conn.commit();
            }
        }

        System.out.println("Reservation load finished");
    }

    private static long applyDiscount(long originalPrice, Coupon coupon) {
        long discounted = (coupon.type == DiscountType.RATE)
                ? originalPrice - (originalPrice * coupon.value / 100)
                : originalPrice - coupon.value;

        if (discounted < 0) discounted = 0;
        if (discounted >= originalPrice) throw new IllegalStateException("쿠폰 할인 규칙 위반");
        return discounted;
    }

    private static void validateEnv() {
        if (DB_HOST == null || DB_PORT == null || DB_NAME == null
                || DB_USER == null || DB_PASSWORD == null) {
            throw new IllegalStateException("환경변수 누락");
        }
    }

    private static List<Long> loadIds(Connection conn, String table) throws SQLException {
        List<Long> ids = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement("SELECT id FROM " + table);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) ids.add(rs.getLong(1));
        }
        return ids;
    }

    private static List<Coupon> loadCoupons(Connection conn) throws SQLException {
        List<Coupon> coupons = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(
                "SELECT name, discount_type, discount_value FROM coupons");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Coupon c = new Coupon();
                c.name = rs.getString("name");
                c.type = DiscountType.valueOf(rs.getString("discount_type"));
                c.value = rs.getLong("discount_value");
                coupons.add(c);
            }
        }
        return coupons;
    }
}
