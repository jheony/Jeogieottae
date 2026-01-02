import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class SpecialPriceCsvGenerator {

    private static final int TOTAL_ROWS = 100;
    private static final String OUTPUT_DIR = "../special_prices/";
    private static final String FILE_NAME = "special_prices_100.csv";
    private static final String[] NAMES = {
            "Summer Special Price",
            "Winter Special Price",
            "Spring Special Price",
            "Autumn Special Price"
    };
    private static final Random RANDOM = new Random();
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) throws IOException {

        File dir = new File(OUTPUT_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(OUTPUT_DIR + FILE_NAME);

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(file),
                        StandardCharsets.UTF_8
                )
        )) {
            writer.write("name,discount,due_timestamp");
            writer.newLine();

            for (int i = 1; i <= TOTAL_ROWS; i++) {

                String name = NAMES[i % NAMES.length];

                long discount = (RANDOM.nextInt(91) + 10) * 1_000L;

                LocalDateTime dueTimestamp =
                        LocalDateTime.now().plusDays(RANDOM.nextInt(90) + 1);

                writer.write(String.format(
                        "%s,%d,%s",
                        name,
                        discount,
                        dueTimestamp.format(FORMATTER)
                ));
                writer.newLine();
            }
        }
    }
}
