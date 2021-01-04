import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CollectTotalThroughput {
    public static void main(String[] args) throws Exception {
        Pattern throughputPattern = Pattern.compile("\"throughput\" : ([0-9.]+),?$");

        Files.walkFileTree(Paths.get(args[0]), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (!file.toFile().getName().equals("statistics.json")) {
                    return FileVisitResult.CONTINUE;
                }

                Path loopCountDir = file.getParent().getParent();
                Path threadNumberDir = loopCountDir.getParent();

                String loop = loopCountDir.getFileName().toString();
                String thread = threadNumberDir.getFileName().toString();

                try {
                    boolean meetTotalSection = false;
                    for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                        if (!meetTotalSection && line.contains("\"Total\" : {")) {
                            meetTotalSection = true;
                        } else if (meetTotalSection) {
                            Matcher matcher = throughputPattern.matcher(line);

                            if (matcher.find()) {
                                String throughput = matcher.group(1);
                                System.out.printf("%s\t%s\t%s%n", thread, loop, throughput);
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                return FileVisitResult.CONTINUE;
            }
        });


    }
}