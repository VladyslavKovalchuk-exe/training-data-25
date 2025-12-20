import java.time.LocalTime;
import java.util.Queue;
import java.util.Arrays;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * Клас BasicDataOperationUsingQueue реалізує роботу з колекціями типу Queue для LocalTime.
 * 
 * <p>Основні функції класу:</p>
 * <ul>
 *   <li>{@link #runDataProcessing()} - Запускає комплекс операцій з даними.</li>
 *   <li>{@link #performArraySorting()} - Упорядковує масив LocalTime.</li>
 *   <li>{@link #findInArray()} - Пошук значення в масиві LocalTime.</li>
 *   <li>{@link #locateMinMaxInArray()} - Знаходить мінімальне і максимальне значення в масиві.</li>
 *   <li>{@link #findInQueue()} - Пошук значення в черзі LocalTime.</li>
 *   <li>{@link #locateMinMaxInQueue()} - Знаходить граничні значення в черзі.</li>
 *   <li>{@link #performQueueOperations()} - Виконує операції peek і poll з чергою.</li>
 * </ul>
 * 
 */
public class BasicDataOperationUsingQueue {
    private LocalTime localTimeValueToSearch;
    private LocalTime[] localTimeArray;
    private Queue<LocalTime> localTimeQueue;

    /**
     * Конструктор, який iнiцiалiзує об'єкт з готовими даними.
     * 
     * @param localTimeValueToSearch Значення для пошуку
     * @param localTimeArray Масив LocalTime
     */
    BasicDataOperationUsingQueue(LocalTime localTimeValueToSearch, LocalTime[] localTimeArray) {
        this.localTimeValueToSearch = localTimeValueToSearch;
        this.localTimeArray = localTimeArray;
        this.localTimeQueue = new PriorityQueue<>(Arrays.asList(localTimeArray));
    }
    
    /**
     * Запускає комплексну обробку даних з використанням черги.
     * 
     * Метод завантажує дані, виконує операції з чергою та масивом LocalTime.
     */
    public void runDataProcessing() {
        // спочатку обробляємо чергу дати та часу
        findInQueue();
        locateMinMaxInQueue();
        performQueueOperations();

        // потім працюємо з масивом
        findInArray();
        locateMinMaxInArray();

        performArraySorting();

        findInArray();
        locateMinMaxInArray();

        // зберігаємо відсортований масив до файлу
        DataFileHandler.writeArrayToFile(localTimeArray, BasicDataOperation.PATH_TO_DATA_FILE + ".sorted");
    }

    /**
     * Сортує масив об'єктiв LocalTime та виводить початковий i вiдсортований масиви.
     * Вимiрює та виводить час, витрачений на сортування масиву в наносекундах.
     */
    private void performArraySorting() {
        long timeStart = System.nanoTime();

        localTimeArray = Arrays.stream(localTimeArray)
            .sorted()
            .toArray(LocalTime[]::new);

        PerformanceTracker.displayOperationTime(timeStart, "упорядкування масиву дати i часу");
    }

    /**
     * Здійснює пошук конкретного значення в масиві дати та часу.
     */
    private void findInArray() {
        long timeStart = System.nanoTime();

        int position = Arrays.stream(localTimeArray)
            .map(Arrays.asList(localTimeArray)::indexOf)
            .filter(i -> localTimeValueToSearch.equals(localTimeArray[i]))
            .findFirst()
            .orElse(-1);

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в масивi дати i часу");

        if (position >= 0) {
            System.out.println("Елемент '" + localTimeValueToSearch + "' знайдено в масивi за позицією: " + position);
        } else {
            System.out.println("Елемент '" + localTimeValueToSearch + "' відсутній в масиві.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в масиві LocalTime.
     */
    private void locateMinMaxInArray() {
        if (localTimeArray == null || localTimeArray.length == 0) {
            System.out.println("Масив є пустим або не ініціалізованим.");
            return;
        }

        long timeStart = System.nanoTime();

        LocalTime minValue = Arrays.stream(localTimeArray)
                                  .min(LocalTime::compareTo)
                                  .orElse(null);


        LocalTime maxValue = Arrays.stream(localTimeArray)
                                  .max(LocalTime::compareTo)
                                  .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в масивi");

        System.out.println("Найменше значення в масивi: " + minValue);
        System.out.println("Найбільше значення в масивi: " + maxValue);
    }

    /**
     * Здійснює пошук конкретного значення в черзі дати та часу.
     */
    private void findInQueue() {
        // вимірюємо час пошуку в черзі
        long timeStart = System.nanoTime();

        boolean elementExists = localTimeQueue.stream()
            .anyMatch(i -> i.equals(localTimeValueToSearch));

        PerformanceTracker.displayOperationTime(timeStart, "пошук елемента в Queue дати i часу");

        if (elementExists) {
            System.out.println("Елемент '" + localTimeValueToSearch + "' знайдено в Queue");
        } else {
            System.out.println("Елемент '" + localTimeValueToSearch + "' відсутній в Queue.");
        }
    }

    /**
     * Визначає найменше та найбільше значення в черзі LocalTime.
     */
    private void locateMinMaxInQueue() {
        if (localTimeQueue == null || localTimeQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        // відстежуємо час пошуку граничних значень
        long timeStart = System.nanoTime();

        LocalTime minValue = localTimeQueue.stream()
            .min(LocalTime::compareTo)
            .orElse(null);

        LocalTime maxValue = localTimeQueue.stream()
            .max(LocalTime::compareTo)
            .orElse(null);

        PerformanceTracker.displayOperationTime(timeStart, "визначення мiнiмальної i максимальної дати в Queue");

        System.out.println("Найменше значення в Queue: " + minValue);
        System.out.println("Найбільше значення в Queue: " + maxValue);
    }

    /**
     * Виконує операції peek і poll з чергою LocalTime.
     */
    private void performQueueOperations() {
        if (localTimeQueue == null || localTimeQueue.isEmpty()) {
            System.out.println("Черга є пустою або не ініціалізованою.");
            return;
        }

        LocalTime headElement = localTimeQueue.peek();
        System.out.println("Головний елемент черги (peek): " + headElement);

        headElement = localTimeQueue.poll();
        System.out.println("Видалений елемент черги (poll): " + headElement);

        headElement = localTimeQueue.peek();
        System.out.println("Новий головний елемент черги: " + headElement);
    }
}