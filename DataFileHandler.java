import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
        LocalTime[] temporaryArray = new LocalTime[1000];
        int currentIndex = 0;

        try (BufferedReader fileReader = new BufferedReader(new FileReader(filePath))) {
            String currentLine;
            while ((currentLine = fileReader.readLine()) != null) {
                // Видаляємо можливі невидимі символи та BOM
                currentLine = currentLine.trim().replaceAll("^\\uFEFF", "");
                if (!currentLine.isEmpty()) {
                    LocalTime parsedDateTime = LocalTime.parse(currentLine, timeFormatter);
                    temporaryArray[currentIndex++] = parsedDateTime;
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        LocalTime[] resultArray = new LocalTime[currentIndex];
        System.arraycopy(temporaryArray, 0, resultArray, 0, currentIndex);

        return resultArray;
    }

    /**
     * Зберігає масив об'єктів LocalTime у файл.
     * 
     * @param dateTimeArray Масив об'єктів LocalTime.
     * @param filePath Шлях до файлу для збереження.
     */
    public static void writeArrayToFile(LocalTime[] dateTimeArray, String filePath) {
        try (BufferedWriter fileWriter = new BufferedWriter(new FileWriter(filePath))) {
            for (LocalTime dateTimeElement : dateTimeArray) {
                fileWriter.write(dateTimeElement.toString());
                fileWriter.newLine();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
