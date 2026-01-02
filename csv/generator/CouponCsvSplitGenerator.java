import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class CouponCsvSplitGenerator {

    private static final int TOTAL_ROWS = 5_000_000;
    private static final int CHUNK_SIZE = 500_000;
    private static final String OUTPUT_DIR = "../coupons/";
    private static final String[] COUPON_NAMES = {
            "Summer Special", "Winter Special", "Spring Special", "Autumn Special"
    };
    private static final String[] DISCOUNT_TYPES = {
            "FIXED", "RATE"
    };
    private static final String[] ACCOMMODATION_TYPES = {
            "HOTEL", "MOTEL", "RESORT", "POOL_VILLA"
    };
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {

        int fileIndex = 1;
        int writtenInFile = 0;
        int totalWritten = 0;

        BufferedWriter writer = createWriter(fileIndex);

        for (int i = 1; i <= TOTAL_ROWS; i++) {

            if (writtenInFile == CHUNK_SIZE) {
                writer.close();
                fileIndex++;
                writtenInFile = 0;
                writer = createWriter(fileIndex);
            }

            String name = COUPON_NAMES[i % COUPON_NAMES.length];
            String discountType = DISCOUNT_TYPES[RANDOM.nextInt(DISCOUNT_TYPES.length)];

            long discountValue;
            if ("RATE".equals(discountType)) {
                discountValue = RANDOM.nextInt(99) + 1;
            } else {
                discountValue = (RANDOM.nextInt(50) + 1) * 1000L;
            }

            long minPrice = RANDOM.nextInt(50_001);

            String accommodationType =
                    ACCOMMODATION_TYPES[RANDOM.nextInt(ACCOMMODATION_TYPES.length)];

            LocalDateTime expiresAt =
                    LocalDateTime.now().plusDays(RANDOM.nextInt(365) + 1);

            writer.write(String.format(
                    "%s,%s,%d,%s,%d,%s",
                    name,
                    discountType,
                    discountValue,
                    expiresAt.format(FORMATTER),
                    minPrice,
                    accommodationType
            ));
            writer.newLine();

            writtenInFile++;
            totalWritten++;

            if (totalWritten % 500_000 == 0) {
                System.out.println(totalWritten + " rows generated");
            }
        }

        writer.close();
    }

    private static BufferedWriter createWriter(int fileIndex) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = String.format(
                OUTPUT_DIR + "coupons_%03d.csv",
                fileIndex
        );

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName),
                        StandardCharsets.UTF_8
                )
        );

        writer.write("name,discount_type,discount_value,expires_at,min_price,accommodation_type");
        writer.newLine();

        return writer;
    }
}
