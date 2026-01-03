import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class AccommodationCsvGenerator {

    private static final int TOTAL_ROWS = 5_000_000;
    private static final int CHUNK_SIZE = 500_000;
    private static final String OUTPUT_DIR = "../accommodations/";
    private static final String[] TYPES = {
            "HOTEL", "MOTEL", "RESORT", "POOL_VILLA"
    };
    private static final String[] CITIES = {
            "SEOUL", "ANYANG", "GWANGJU", "CHEONAN", "BUSAN"
    };
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws IOException {

        int fileIndex = 1;
        int writtenInFile = 0;

        BufferedWriter writer = createWriter(fileIndex);

        for (int i = 1; i <= TOTAL_ROWS; i++) {

            if (writtenInFile == CHUNK_SIZE) {
                writer.close();
                fileIndex++;
                writtenInFile = 0;
                writer = createWriter(fileIndex);
            }

            String type = TYPES[i % TYPES.length];
            String city = CITIES[i % CITIES.length];

            String name = String.format(
                    "%s %s %d",
                    type,
                    city,
                    i
            );

            double rating = Math.round(RANDOM.nextDouble() * 50) / 10.0;

            long viewCount = RANDOM.nextInt(1_000_001);

            writer.write(String.format(
                    "%s,%s,%s,%.1f,%d",
                    name,
                    type,
                    city,
                    rating,
                    viewCount
            ));
            writer.newLine();

            writtenInFile++;

            if (i % 100_000 == 0) {
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
                OUTPUT_DIR + "accommodations_%03d.csv",
                fileIndex
        );

        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(fileName),
                        StandardCharsets.UTF_8
                )
        );

        writer.write("name,type,location,rating,view_count");
        writer.newLine();

        return writer;
    }
}
