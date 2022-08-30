package HW4;

//25.08.2022    ДЗ Ломтева Ю.В.
//  Урок 4. Расширенные возможности rest-assured
//  Работа со Spoonacular API
//  1. Отрефакторьте код проверок и добавьте дополнительные тесты для запросов из цепочки Shopping List,
//  используя rest-assured.
//  2. Воспользуйтесь кейсами из ПЗ № 2 и 3, перенеся всю логику из постман-коллекции в код.
//  3. Сдайте ссылку на репозиторий, указав ветку с кодом.
//      Главные критерии для проверки — отсутствие хардкода в коде тестов и наличие тестов на запросы
//      Add to Shopping list (POST /mealplanner/:username/shopping-list/items),
//      Get Shopping List (GET /mealplanner/:username/shopping-list) и
//      Delete from Shopping list (DELETE /mealplanner/:username/shopping-list/items/:id).

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// неизменяемая часть - class AbstractTest, используются в тестовых классах
// изменяемая часть считывается из src/main/resources/my.properties
public abstract class AbstractTest {
    static Properties prop = new Properties();
//        private static String apiKey;
    private static String baseUrl;
    private static String hashYuriy1305;
    protected static ResponseSpecification responseSpecification;
    protected static RequestSpecification requestSpecification;

    @BeforeAll
    static void initTest() throws IOException {
// т.к. для всех тестов, переносим сюда:
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        InputStream configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);
//        apiKey = prop.getProperty("apiKey");
        baseUrl = prop.getProperty("base_url");
        hashYuriy1305 = prop.getProperty("hashYuriy1305");

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.ALL)
                .build();

        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", prop.getProperty("apiKey"))
                .addQueryParam("language", "en")
                .setContentType(ContentType.JSON)
                .build();
// для глобальности:
        RestAssured.responseSpecification = responseSpecification;
        RestAssured.requestSpecification = requestSpecification;
    }

// излишне при глобальности requestSpecification
// public static String getApiKey() {
//        return apiKey;
//    }
    public static String getBaseUrl() {
        return baseUrl;
    }
    public static String gethash() { return hashYuriy1305; }
}