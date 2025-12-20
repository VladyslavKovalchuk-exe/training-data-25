import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Клас BasicDataOperationUsingMap реалізує операції з колекціями типу Map для зберігання пар ключ-значення.
 * 
 * <p>Методи класу:</p>
 * <ul>
 *   <li>{@link #executeDataOperations()} - Виконує комплекс операцій з даними Map.</li>
 *   <li>{@link #findByKey()} - Здійснює пошук елемента за ключем в Map.</li>
 *   <li>{@link #findByValue()} - Здійснює пошук елемента за значенням в Map.</li>
 *   <li>{@link #addEntry()} - Додає новий запис до Map.</li>
 *   <li>{@link #removeByKey()} - Видаляє запис з Map за ключем.</li>
 *   <li>{@link #removeByValue()} - Видаляє записи з Map за значенням.</li>
 *   <li>{@link #sortByKey()} - Сортує Map за ключами.</li>
 *   <li>{@link #sortByValue()} - Сортує Map за значеннями.</li>
 * </ul>
 */
public class BasicDataOperationUsingMap {
    private final Boa KEY_TO_SEARCH_AND_DELETE = new Boa("Тайсон", "Каракас");
    private final Boa KEY_TO_ADD = new Boa("Зорро", "Сан-Хосе");

    private final String VALUE_TO_SEARCH_AND_DELETE = "Олександр";
    private final String VALUE_TO_ADD = "Богдан";

    private HashMap<Boa, String> hashmap;
    private LinkedHashMap<Boa, String> linkedHashmap;

    /**
     * Компаратор для сортування Map.Entry за значеннями String.
     * Використовує метод String.compareTo() для порівняння імен власників.
     */
    static class OwnerValueComparator implements Comparator<Map.Entry<Boa, String>> {
        @Override
        public int compare(Map.Entry<Boa, String> e1, Map.Entry<Boa, String> e2) {
            String v1 = e1.getValue();
            String v2 = e2.getValue();
            if (v1 == null && v2 == null) return 0;
            if (v1 == null) return -1;
            if (v2 == null) return 1;
            return v1.compareTo(v2);
        }
    }

    /**
     * Внутрішній клас Pet для зберігання інформації про домашню тварину.
     * 
     * Реалізує Comparable<Pet> для визначення природного порядку сортування.
     * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
     */
    public static class Boa implements Comparable<Boa> {
        private final String nickname;
        private final String birthCity;

        public Boa(String nickname) {
            this.nickname = nickname;
            this.birthCity = null;
        }

        public Boa(String nickname, String species) {
            this.nickname = nickname;
            this.birthCity = species;
        }

        public String getNickname() { 
            return nickname; 
        }

        public String getBirthCity() {
            return birthCity;
        }

        /**
         * Порівнює цей об'єкт Pet з іншим для визначення порядку сортування.
         * Природний порядок: спочатку за кличкою (nickname) за зростанням, потім за видом (species) за спаданням.
         * 
         * @param other Pet об'єкт для порівняння
         * @return негативне число, якщо цей Pet < other; 
         *         0, якщо цей Pet == other; 
         *         позитивне число, якщо цей Pet > other
         * 
         * Критерій порівняння: поля nickname (кличка) за зростанням та species (вид) за спаданням.
         * 
         * Цей метод використовується:
         * - LinkedHashMap для автоматичного сортування ключів Pet за nickname (зростання), потім за species (спадання)
         * - Collections.sort() для сортування Map.Entry за ключами Pet
         * - Collections.binarySearch() для пошуку в відсортованих колекціях
         */
        @Override
        public int compareTo(Boa other) {
            if (other == null) return 1;
            
            // Спочатку порівнюємо за кличкою (за зростанням; null у кінці)
            int nicknameComparison;
            if (this.nickname == null && other.nickname == null) {
                nicknameComparison = 0;
            } else if (this.nickname == null) {
                nicknameComparison = 1;
            } else if (other.nickname == null) {
                nicknameComparison = -1;
            } else {
                nicknameComparison = other.nickname.compareTo(this.nickname);
            }
            
            // Якщо клички різні, повертаємо результат
            if (nicknameComparison != 0) {
                return nicknameComparison;
            }
            
            // Якщо клички однакові, порівнюємо за містом (за спаданням - інвертуємо результат)
            if (this.birthCity == null && other.birthCity == null) return 0;
            if (this.birthCity == null) return 1;  // null йде в кінець при спаданні
            if (other.birthCity == null) return -1;
            return other.birthCity.compareTo(this.birthCity);  // Інвертоване порівняння для спадання
        }

        /**
         * Перевіряє рівність цього Pet з іншим об'єктом.
         * Два Pet вважаються рівними, якщо їх клички (nickname) та види (species) однакові.
         * 
         * @param obj об'єкт для порівняння
         * @return true, якщо об'єкти рівні; false в іншому випадку
         * 
         * Критерій рівності: поля nickname (кличка) та species (вид).
         * 
         * Важливо: метод узгоджений з compareTo() - якщо equals() повертає true,
         * то compareTo() повертає 0, оскільки обидва методи порівнюють за nickname та species.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Boa pet = (Boa) obj;
            
            boolean nicknameEquals = nickname != null ? nickname.equals(pet.nickname) : pet.nickname == null;
            boolean speciesEquals = birthCity != null ? birthCity.equals(pet.birthCity) : pet.birthCity == null;
            
            return nicknameEquals && speciesEquals;
        }

        /**
         * Повертає хеш-код для цього Pet.
         * 
         * @return хеш-код, обчислений на основі nickname та species
         * 
         * Базується на полях nickname та species для узгодженості з equals().
         * 
         * Важливо: узгоджений з equals() - якщо два Pet рівні за equals()
         * (мають однакові nickname та species), вони матимуть однаковий hashCode().
         */
        @Override
        public int hashCode() {
            // Початкове значення: хеш-код поля nickname (або 0, якщо nickname == null)
            int result = nickname != null ? nickname.hashCode() : 0;
            
            // Комбінуємо хеш-коди полів за формулою: result = 31 * result + hashCode(поле)
            // Множник 31 - просте число, яке дає хороше розподілення хеш-кодів
            // і оптимізується JVM як (result << 5) - result
            // Додаємо хеш-код виду (або 0, якщо species == null) до загального результату
            result = 31 * result + (birthCity != null ? birthCity.hashCode() : 0);
            
            return result;
        }

        /**
         * Повертає строкове представлення Pet.
         * 
         * @return кличка тварини (nickname), вид (species) та hashCode
         */
        @Override
        public String toString() {
            if (birthCity != null) {
                return "Boa{nickname='" + nickname + "', birthCity='" + birthCity + "', hashCode=" + hashCode() + "}";
            }
            return "Boa{nickname='" + nickname + "', hashCode=" + hashCode() + "}";
        }
    }

    /**
     * Конструктор, який ініціалізує об'єкт з готовими даними.
     * 
     * @param hashmap HashMap з початковими даними (ключ: Pet, значення: ім'я власника)
     * @param linkedHashmap LinkedHashMap з початковими даними (ключ: Pet, значення: ім'я власника)
     */
    BasicDataOperationUsingMap(HashMap<Boa, String> hashmap, LinkedHashMap<Boa, String> linkedHashmap) {
        this.hashmap = hashmap;
        this.linkedHashmap = linkedHashmap;
    }
    
    /**
     * Виконує комплексні операції з Map.
     * 
     * Метод виконує різноманітні операції з Map: пошук, додавання, видалення та сортування.
     */
    public void executeDataOperations() {
        // Спочатку працюємо з HashMap
        System.out.println("========= Операції з HashMap =========");
        System.out.println("Початковий розмір HashMap: " + hashmap.size());
        
        // Пошук до сортування
        findByKeyInHashMap();
        findByValueInHashMap();

        printHashMap();
        sortHashMap();
        printHashMap();

        // Пошук після сортування
        findByKeyInHashMap();
        findByValueInHashMap();

        addEntryToHashMap();
        
        removeByKeyFromHashMap();
        removeByValueFromHashMap();
               
        System.out.println("Кінцевий розмір HashMap: " + hashmap.size());

        // Потім обробляємо LinkedHashMap
        System.out.println("\n\n========= Операції з LinkedHashMap =========");
        System.out.println("Початковий розмір LinkedHashMap: " + linkedHashmap.size());
        
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        printLinkedHashMap();
        sortLinkedHashMap();
        printLinkedHashMap();

        // Пошук після сортування
        findByKeyInLinkedHashMap();
        findByValueInLinkedHashMap();

        addEntryToLinkedHashMap();
        
        removeByKeyFromLinkedHashMap();
        removeByValueFromLinkedHashMap();
        
        System.out.println("Кінцевий розмір LinkedHashMap: " + linkedHashmap.size());
    }


    // ===== Методи для HashMap =====

    /**
     * Виводить вміст HashMap без сортування.
     * HashMap не гарантує жодного порядку елементів.
     */
    private void printHashMap() {
        System.out.println("\n=== Пари ключ-значення в HashMap ===");
        long timeStart = System.nanoTime();

        for (Map.Entry<Boa, String> entry : hashmap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пари ключ-значення в HashMap");
    }

    /**
     * Сортує HashMap за ключами.
     * Використовує Collections.sort() з природним порядком Pet (Pet.compareTo()).
     * Перезаписує hashmap відсортованими даними.
     */
    private void sortHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Pet
        List<Boa> sortedKeys = new ArrayList<>(hashmap.keySet());
        Collections.sort(sortedKeys);
        
        // Створюємо нову мапу з відсортованими ключами (щоб зберегти порядок ітерації)
        LinkedHashMap<Boa, String> sortedHashMap = new LinkedHashMap<>();
        for (Boa key : sortedKeys) {
            sortedHashMap.put(key, hashmap.get(key));
        }
        
        // Перезаписуємо оригінальну hashmap
        hashmap = sortedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування HashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в HashMap.
     * Використовує Pet.hashCode() та Pet.equals() для пошуку.
     */
    void findByKeyInHashMap() {
        long timeStart = System.nanoTime();

        boolean found = hashmap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в HashMap");

        if (found) {
            String value = hashmap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в HashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        List<Map.Entry<Boa, String>> entries = new ArrayList<>(hashmap.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Boa, String> searchEntry = new Map.Entry<Boa, String>() {
            public Boa getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в HashMap");

        if (position >= 0) {
            Map.Entry<Boa, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Pet: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в HashMap.");
        }
    }

    /**
     * Додає новий запис до HashMap.
     */
    void addEntryToHashMap() {
        long timeStart = System.nanoTime();

        hashmap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до HashMap");

        System.out.println("Додано новий запис: Pet='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з HashMap за ключем.
     */
    void removeByKeyFromHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = hashmap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з HashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з HashMap за значенням.
     */
    void removeByValueFromHashMap() {
        long timeStart = System.nanoTime();

        List<Boa> keysToRemove = new ArrayList<>();
        for (Map.Entry<Boa, String> entry : hashmap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (Boa key : keysToRemove) {
            hashmap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з HashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    // ===== Методи для LinkedHashMap =====

    /**
     * Виводить вміст LinkedHashMap.
     * LinkedHashMap зберігає порядок вставки (не сортує автоматично).
     */
    private void printLinkedHashMap() {
        System.out.println("\n=== Пари ключ-значення в LinkedHashMap ===");

        long timeStart = System.nanoTime();
        for (Map.Entry<Boa, String> entry : linkedHashmap.entrySet()) {
            System.out.println("  " + entry.getKey() + " -> " + entry.getValue());
        }

        PerformanceTracker.displayOperationTime(timeStart, "виведення пар ключ-значення в LinkedHashMap");
    }

    /**
     * Сортує LinkedHashMap за ключами.
     * Використовує Collections.sort() з природним порядком Boa (Boa.compareTo()).
     * Перезаписує linkedHashmap відсортованими даними.
     */
    private void sortLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список ключів і сортуємо за природним порядком Pet
        List<Boa> sortedKeys = new ArrayList<>(linkedHashmap.keySet());
        Collections.sort(sortedKeys);
        
        // Створюємо нову LinkedHashMap з відсортованими ключами
        LinkedHashMap<Boa, String> sortedLinkedHashMap = new LinkedHashMap<>();
        for (Boa key : sortedKeys) {
            // ВАЖЛИВО: значення треба брати саме з linkedHashmap, а не з hashmap
            sortedLinkedHashMap.put(key, linkedHashmap.get(key));
        }
        
        // Перезаписуємо оригінальну linkedHashmap
        linkedHashmap = sortedLinkedHashMap;

        PerformanceTracker.displayOperationTime(timeStart, "сортування LinkedHashMap за ключами");
    }

    /**
     * Здійснює пошук елемента за ключем в LinkedHashMap.
     * Використовує Pet.compareTo() для навігації по дереву.
     */
    void findByKeyInLinkedHashMap() {
        long timeStart = System.nanoTime();

        boolean found = linkedHashmap.containsKey(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "пошук за ключем в LinkedHashMap");

        if (found) {
            String value = linkedHashmap.get(KEY_TO_SEARCH_AND_DELETE);
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' знайдено. Власник: " + value);
        } else {
            System.out.println("Елемент з ключем '" + KEY_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Здійснює пошук елемента за значенням в LinkedHashMap.
     * Сортує список Map.Entry за значеннями та використовує бінарний пошук.
     */
    void findByValueInLinkedHashMap() {
        long timeStart = System.nanoTime();

        // Створюємо список Entry та сортуємо за значеннями
        List<Map.Entry<Boa, String>> entries = new ArrayList<>(linkedHashmap.entrySet());
        OwnerValueComparator comparator = new OwnerValueComparator();
        Collections.sort(entries, comparator);

        // Створюємо тимчасовий Entry для пошуку
        Map.Entry<Boa, String> searchEntry = new Map.Entry<Boa, String>() {
            public Boa getKey() { return null; }
            public String getValue() { return VALUE_TO_SEARCH_AND_DELETE; }
            public String setValue(String value) { return null; }
        };

        int position = Collections.binarySearch(entries, searchEntry, comparator);

        PerformanceTracker.displayOperationTime(timeStart, "бінарний пошук за значенням в LinkedHashMap");

        if (position >= 0) {
            Map.Entry<Boa, String> foundEntry = entries.get(position);
            System.out.println("Власника '" + VALUE_TO_SEARCH_AND_DELETE + "' знайдено. Pet: " + foundEntry.getKey());
        } else {
            System.out.println("Власник '" + VALUE_TO_SEARCH_AND_DELETE + "' відсутній в LinkedHashMap.");
        }
    }

    /**
     * Додає новий запис до LinkedHashMap.
     */
    void addEntryToLinkedHashMap() {
        long timeStart = System.nanoTime();

        linkedHashmap.put(KEY_TO_ADD, VALUE_TO_ADD);

        PerformanceTracker.displayOperationTime(timeStart, "додавання запису до LinkedHashMap");

        System.out.println("Додано новий запис: Pet='" + KEY_TO_ADD + "', власник='" + VALUE_TO_ADD + "'");
    }

    /**
     * Видаляє запис з LinkedHashMap за ключем.
     */
    void removeByKeyFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        String removedValue = linkedHashmap.remove(KEY_TO_SEARCH_AND_DELETE);

        PerformanceTracker.displayOperationTime(timeStart, "видалення за ключем з LinkedHashMap");

        if (removedValue != null) {
            System.out.println("Видалено запис з ключем '" + KEY_TO_SEARCH_AND_DELETE + "'. Власник був: " + removedValue);
        } else {
            System.out.println("Ключ '" + KEY_TO_SEARCH_AND_DELETE + "' не знайдено для видалення.");
        }
    }

    /**
     * Видаляє записи з LinkedHashMap за значенням.
     */
    void removeByValueFromLinkedHashMap() {
        long timeStart = System.nanoTime();

        List<Boa> keysToRemove = new ArrayList<>();
        for (Map.Entry<Boa, String> entry : linkedHashmap.entrySet()) {
            if (entry.getValue() != null && entry.getValue().equals(VALUE_TO_SEARCH_AND_DELETE)) {
                keysToRemove.add(entry.getKey());
            }
        }
        
        for (Boa key : keysToRemove) {
            linkedHashmap.remove(key);
        }

        PerformanceTracker.displayOperationTime(timeStart, "видалення за значенням з LinkedHashMap");

        System.out.println("Видалено " + keysToRemove.size() + " записів з власником '" + VALUE_TO_SEARCH_AND_DELETE + "'");
    }

    /**
     * Головний метод для запуску програми.
     */
    public static void main(String[] args) {
        // Створюємо початкові дані (ключ: Pet, значення: ім'я власника)
        HashMap<Boa, String> hashmap = new HashMap<>();
        hashmap.put(new Boa("Яшма", "Сан-Паулу"), "Олександр");
        hashmap.put(new Boa("Фантом", "Ріо-де-Жанейро"), "Наталія");
        hashmap.put(new Boa("Удав", "Мехіко"), "Ірина");
        hashmap.put(new Boa("Тайсон", "Каракас"), "Дмитро");
        hashmap.put(new Boa("Сіріус", "Богота"), "Олександр");
        hashmap.put(new Boa("Пітон", "Ліма"), "Софія");
        hashmap.put(new Boa("Немо", "Гвантемала"), "Наталія");
        hashmap.put(new Boa("Мідас", "Буенос-Айрес"), "Андрій");
        hashmap.put(new Boa("Лорд", "Сантьяго"), "Марія");
        hashmap.put(new Boa("Кобра", "Панама"), "Ірина");

        LinkedHashMap<Boa, String> linkedHashMap = new LinkedHashMap<Boa, String>() {{
            put(new Boa("Яшма", "Сан-Паулу"), "Олександр");
            put(new Boa("Фантом", "Ріо-де-Жанейро"), "Наталія");
            put(new Boa("Удав", "Мехіко"), "Ірина");
            put(new Boa("Тайсон", "Каракас"), "Дмитро");
            put(new Boa("Сіріус", "Богота"), "Олександр");
            put(new Boa("Пітон", "Ліма"), "Софія");
            put(new Boa("Немо", "Гвантемала"), "Наталія");
            put(new Boa("Мідас", "Буенос-Айрес"), "Андрій");
            put(new Boa("Лорд", "Сантьяго"), "Марія");
            put(new Boa("Кобра", "Панама"), "Ірина");

        }};

        // Створюємо об'єкт і виконуємо операції
        BasicDataOperationUsingMap operations = new BasicDataOperationUsingMap(hashmap, linkedHashMap);
        operations.executeDataOperations();
    }
}
