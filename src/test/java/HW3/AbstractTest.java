package HW3;

//15.08.2022    ДЗ Ломтева Ю.В.
// Урок 3. Автоматизированное тестирование REST API с использованием rest-assured
//   1. Автоматизируйте GET /recepies/complexSearch (минимум 5 кейсов) и POST /recipes/cuisine (минимум 5 кейсов),
//      используя rest-assured.
//   2. Сделать автоматизацию цепочки (хотя бы 1 тест со всеми эндпоинтами) для создания и удаления блюда в MealPlan).
//      Подумайте, как использовать tearDown при тестировании POST /mealplanner/:username/shopping-list/items.
//   3. Воспользуйтесь кейсами, которые вы написали в ДЗ №2, перенеся всю логику из постман-коллекции в код.
//   4. Сдайте ссылку на репозиторий, указав ветку с кодом.

import org.junit.jupiter.api.BeforeAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// неизменяемая часть - class AbstractTest, используются в тестовых классах
// изменяемая часть считывается из src/main/resources/my.properties
public abstract class AbstractTest {

    static Properties prop = new Properties();
    private static String apiKey;
    private static String baseUrl;

    @BeforeAll
    static void initTest() throws IOException {
        InputStream configFile = new FileInputStream("src/main/my.properties");
        prop.load(configFile);

        apiKey =  prop.getProperty("apiKey");
        baseUrl= prop.getProperty("base_url");
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}