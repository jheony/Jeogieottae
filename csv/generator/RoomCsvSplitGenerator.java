import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class RoomCsvSplitGenerator {

    private static final int TOTAL_ROWS = 5_000_000;
    private static final int CHUNK_SIZE = 500_000;
    private static final String OUTPUT_DIR = "../rooms/";
    private static final String[] ROOM_TYPES = {
            "Business Room",
            "Suite Room"
    };
    private static final int SPECIAL_PRICE_COUNT = 100;
    private static final Random RANDOM = new Random();
    public static void main(String[] args) throws IOException {

        int fileIndex = 1;
        int written = 0;

        BufferedWriter writer = createWriter(fileIndex);

        for (long i = 1; i <= TOTAL_ROWS; i++) {

            if (written == CHUNK_SIZE) {
                writer.close();
                fileIndex++;
                written = 0;
                writer = createWriter(fileIndex);
            }

            long logicalNo = i;

            String roomType = ROOM_TYPES[(int) (logicalNo % ROOM_TYPES.length)];
            String name = roomType + " " + logicalNo;

            long price = calcRoomPrice(logicalNo);

            long accommodationId = ((logicalNo - 1) / 10) + 1;

            Long specialPriceId = null;
            if (RANDOM.nextInt(3) == 0) {
                specialPriceId = (long) (RANDOM.nextInt(SPECIAL_PRICE_COUNT) + 1);
            }

            writer.write(
                    name + "," +
                            price + "," +
                            accommodationId + "," +
                            (specialPriceId == null ? "" : specialPriceId)
            );

            writer.newLine();
            written++;

            if (logicalNo % 500_000 == 0) {
                System.out.println(logicalNo + " rooms generated");
            }
        }

        writer.close();
    }

    private static long calcRoomPrice(long logicalNo) {
        return (((logicalNo * 37) % 100) + 1) * 100_000L;
    }

    private static BufferedWriter createWriter(int index) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) dir.mkdirs();

        String fileName = OUTPUT_DIR + String.format("rooms_%03d.csv", index);

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName),
                        StandardCharsets.UTF_8
                )
        );

        writer.write("name,price,accommodation_id,special_price_id");
        writer.newLine();

        return writer;
    }
}
