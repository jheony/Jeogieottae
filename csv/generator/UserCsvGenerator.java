import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class UserCsvGenerator {

    private static final String[] LAST_NAMES = {
            "Kim", "Lee", "Park", "Choi", "Jung",
            "Kang", "Yoon", "Lim", "Oh", "Han",
            "Shin", "Cho", "Bae", "Ryu", "Cha",
            "Noh", "Moon", "Baek", "Seo", "Hong"
    };
    private static final String[] FIRST_NAMES = {
            "Minsoo", "Seoyeon", "Jihoon", "Hyunwoo", "Sujin",
            "Doyoon", "Yerin", "Hajun", "Jiwoo", "Seojun",
            "Minjae", "Eunbyul", "Siyoon", "Taeyang", "Yujin",
            "Daeun", "Jaemin", "Soyul", "Woojin", "Chaewon"
    };
    private static final String ENCODED_PASSWORD =
            "$2a$10$7EqJtq98hPqEX7fNZaFWoOhi5Zp8oG9yZtM6xUu6YvJzN6zG6nX2K";
    private static final String OUTPUT_DIR = "../users/";
    private static final String FILE_NAME = "users_100.csv";

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
            writer.write("email,username,password,is_deleted");
            writer.newLine();

            for (int i = 1; i <= 100; i++) {
                String email = "user" + i + "@test.com";
                String lastName = LAST_NAMES[(i - 1) % LAST_NAMES.length];
                String firstName = FIRST_NAMES[(i - 1) % FIRST_NAMES.length];
                String username = lastName + firstName;

                writer.write(String.format(
                        "%s,%s,%s,0",
                        email,
                        username,
                        ENCODED_PASSWORD
                ));
                writer.newLine();
            }
        }
    }
}
