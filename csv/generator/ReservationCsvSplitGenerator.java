import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class ReservationCsvSplitGenerator {

    private static final int TOTAL_ROWS = 5_000_000;
    private static final int CHUNK_SIZE = 500_000;
    private static final String OUTPUT_DIR = "../reservations/";
    private static final int USER_COUNT = 100;
    private static final int ROOM_COUNT = 5_000_000;
    private static final CouponDef[] COUPONS = {
            new CouponDef("Summer Special", "FIXED", 200_000, null),
            new CouponDef("Winter Special", "FIXED", 500_000, null),
            new CouponDef("Spring Special", "RATE", 15, null),
            new CouponDef("Autumn Special", "RATE", 25, null)
    };
    private static final Random R = new Random();
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static long calcRoomPrice(long roomId) {
        return (((roomId * 37) % 100) + 1) * 100_000L;
    }
    private static long applyDiscount(long original, CouponDef c) {
        if (c == null) return original;

        if ("FIXED".equals(c.type)) {
            long discounted = original - c.value;
            return Math.max(0, discounted);
        }
        long rate = c.value;
        return (original * (100 - rate)) / 100;
    }

    public static void main(String[] args) throws Exception {
        int fileIndex = 1;
        int writtenInFile = 0;

        BufferedWriter w = createWriter(fileIndex);

        for (int i = 1; i <= TOTAL_ROWS; i++) {

            if (writtenInFile == CHUNK_SIZE) {
                w.close();
                fileIndex++;
                writtenInFile = 0;
                w = createWriter(fileIndex);
            }

            long userId = R.nextInt(USER_COUNT) + 1L;
            long roomId = R.nextInt(ROOM_COUNT) + 1L;

            LocalDateTime checkIn = LocalDateTime.now().plusDays(R.nextInt(180)); // 0~179일 후
            int stayDays = R.nextInt(14) + 1; // 1~14박
            LocalDateTime checkOut = checkIn.plusDays(stayDays);

            long guestCount = R.nextInt(100) + 1L;

            long originalPrice = calcRoomPrice(roomId);

            CouponDef coupon = null;
            if (R.nextInt(10) < 4) {
                coupon = COUPONS[R.nextInt(COUPONS.length)];
            }

            String couponName = (coupon == null) ? "" : coupon.name;
            long discountedPrice = applyDiscount(originalPrice, coupon);

            String status;
            int s = R.nextInt(10);
            if (s < 6) status = "RESERVED";
            else if (s < 9) status = "VISITED";
            else status = "CANCEL";

            LocalDateTime createdAt = checkIn.minusDays(R.nextInt(30) + 1);

            int isDeleted = (R.nextInt(1000) == 0) ? 1 : 0;

            // couponName이 비면 컬럼을 빈 값으로 두고, LOAD DATA에서 NULL 처리
            w.write(String.format(
                    "%s,%s,%d,%d,%s,%d,%d,%d,%s,%s,%d",
                    checkIn.format(F),
                    checkOut.format(F),
                    userId,
                    roomId,
                    couponName,
                    guestCount,
                    originalPrice,
                    discountedPrice,
                    status,
                    createdAt.format(F),
                    isDeleted
            ));
            w.newLine();

            writtenInFile++;

            if (i % 500_000 == 0) System.out.println(i + " rows generated");
        }

        w.close();
    }

    private static BufferedWriter createWriter(int idx) throws IOException {
        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) dir.mkdirs();

        String fileName = String.format(OUTPUT_DIR + "reservations_%03d.csv", idx);

        BufferedWriter w = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)
        );

        w.write("check_in,check_out,user_id,room_id,coupon_name,guest_count,original_price,discounted_price,status,created_at,is_deleted");
        w.newLine();
        return w;
    }

    private static class CouponDef {
        final String name;
        final String type;
        final long value;  // FIXED=amount, RATE=percent
        final Long minPrice;
        CouponDef(String name, String type, long value, Long minPrice) {
            this.name = name;
            this.type = type;
            this.value = value;
            this.minPrice = minPrice;
        }
    }
}
