import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Клас DataFileHandler управляє роботою з файлами даних LocalTime.
 */
public class DataFileHandler {
    /**
     * Завантажує масив об'єктів LocalTime з файлу.
     * 
     * @param filePath Шлях до файлу з даними.
     * @return Масив об'єктів LocalTime.
     */
    public static LocalTime[] loadArrayFromFile(String filePath) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_TIME;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            return fileReader.lines()
                    .map(currentLine -> currentLine.trim().replaceAll("^\\uFEFF", ""))
                    .filter(currentLine -> !currentLine.isEmpty())
                    .map(currentLine -> LocalTime.parse(currentLine, timeFormatter))
                    .toArray(LocalTime[]::new);
        } catch (IOException ioException) {
            throw new RuntimeException("Помилка читання даних з файлу: " + filePath, ioException);
        }
    }

    /**
     * Зберігає масив об'єктів LocalTime у файл.
     * 
     * @param dateTimeArray Масив об'єктів LocalTime.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(LocalTime[] dateTimeArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            String content = Arrays.stream(dateTimeArray)
                    .map(LocalTime::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
           
            fileWriter.write(content);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
