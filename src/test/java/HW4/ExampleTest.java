package HW4;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ExampleTest extends AbstractTest {
    private static String get_path = "recipes/complexSearch";
    private static String post_path = "recipes/cuisine";
    private static String recipeTitle;

    @Test
    void getTests() {
// 1й GET из HW2 - код 200 и рецепты нашли:
        Integer totalResults = given()
//  здесь и далее используем глобальность  response/request Specification
//                .queryParam("apiKey", getApiKey())
                .when()
                .get(getBaseUrl() + get_path)
                .then()
                .extract()
                .jsonPath()
                .get("totalResults");
        // простая проверка найденного, проверка кода - в глобальном запросе:
        if (totalResults > 0) {
            System.out.println("GETs:\n1: Успешно, код: 200.\n" + "Рецептов нашли, штук - " + totalResults);
        } else {
            System.out.println("1: Успешно, код: 200,\n" + " но ничего не нашли.");
        }
// 2й GET из HW2 - 2 низколорийных безглютеновых блюда итальянской кухни:
        totalResults = given()
                .queryParam("cuisine", "Italian")
                .queryParam("intolerances", "gluten")
                .queryParam("maxCalories", "25")
                // варианты логирования запроса: .log().method() / log().params()
//                .log().all()
                // "предпроверка" на 2 блюда:
                .response()
                .expect()
                .body("totalResults", equalTo(2))
                //
                .when()
                .get(getBaseUrl() + get_path)
                .then()
                .extract()
                .jsonPath()
                .get("totalResults");
        // проверка наглядно:
        System.out.println("2: Нашли " + totalResults + " низколорийных безглютеновых блюда итальянской кухни.");
// 3й GET из HW2 - несуществующие инградиенты:
        totalResults = given()
                .queryParam("includeIngredients", "1,2,3")
                // "предпроверка" на 0 блюд:
                .response()
                .expect()
                .body("totalResults", equalTo(0))
                //
                .when()
                .get(getBaseUrl() + get_path)
                .then()
                .extract()
                .jsonPath()
                .get("totalResults");
        // проверка наглядно:
        System.out.println("3: Нашли " + totalResults + " блюд с несуществующими ингредиентами.");
// 4й GET из HW2 - мясо с рыбой и картошечкой и др. параметры:
        totalResults = given()
                .queryParam("includeIngredients", "meat,fish,potato")
                .queryParam("minIron", "10")
                .queryParam("minSugar", "10")
                .queryParam("minCalories", "100")
                .queryParam("minMagnesium", "10")
                // "предпроверка" на 1 блюдо:
                .response()
                .expect()
                .body("totalResults", equalTo(1))
                //
                .when()
                .get(getBaseUrl() + get_path)
                .then()
                .extract()
                .jsonPath()
                .get("totalResults");
        // проверка наглядно:
        System.out.println("4: Нашли рыбно-мясных блюд с прочими ограничениями - " + totalResults + ".");
// 5й GET из HW2 - ищем рецепт индийской кухни для передачи в POST:
        JsonPath response = given()
                .queryParam("cuisine", "Indian")
                .when()
                .get(getBaseUrl() + get_path)
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), not(0));
        recipeTitle = response.get("results.title[0]");
        System.out.println("5: Название блюда индийской кухни: " + recipeTitle);
    }

    @Test
    void postTests() {
// 1й POST: без параметров блюда находим кухню "по умолчанию":
        String cuisine = given()
                .when()
                .post(getBaseUrl() + post_path)
                .path("cuisine");
        assertThat(cuisine, anyOf(containsString("Italian"),
                containsString("Mediterranean"),
                containsString("European")));
        System.out.println("POSTs\n1: Кухня при запросе без параметров (ответ по умолчанию) итальянско-средиземноморско-европейская: " + cuisine);
// 2й POST: ищем бургер с тунцом (!) и находим в американской (!) кухне, похоже, инградиенты роли не играют:
        cuisine = given()
//                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Burger")
                .formParam("ingredientList", "tuna")
                .when()
                .post(getBaseUrl() + post_path)
                .then()
                .extract()
                .jsonPath()
                .get("cuisine")
                .toString();
        System.out.println("2: Бургер с тунцом оказался - " + cuisine + "!");
// 3й POST: Правда ли, традиционнная китайская кухня и здесь китайская?:
        cuisine = given()
                .formParam("title", "Traditional Chinese Recipes")
                .when()
                .post(getBaseUrl() + post_path)
                .path("cuisine");
        assertThat(cuisine, equalTo("Chinese"));
        System.out.println("3: Традиционная Китайская кухня - " + cuisine);
// 4й POST: Ну и проверим чьи только что съеденные чурросы:
        JsonPath response = given()
                .formParam("title", "Churros")
                .when()
                .post(getBaseUrl() + post_path)
                .body()
                .jsonPath();
        assertThat(response.get("cuisines[1]"), equalTo("Spanish"));
        System.out.println("4: Чурросы - " + response.get("cuisines[1]") + ". Угадал! :)");
// 5й POST: С title из GET (индийский рецерт - правда ли, индийская кухня?)
// !!!  и не забываем запускать 5й GET тест, т.к. переменная=рецепт оттуда!!!
        cuisine = given()
                .formParam("title", recipeTitle)
                .when()
                .post(getBaseUrl() + post_path)
                // варианты логирования ответа: print() / prettyPrint()
//                .prettyPeek()
                .path("cuisine");
        assertThat(cuisine, equalTo("Indian"));
        System.out.println("5: Индийский рецепт из GET действительно индийский? " + cuisine.equals("Indian"));
    }

    @Test
    void addDish_getList_deleteDishTest() {
        String urlChain = getBaseUrl() + "mealplanner/yuriy1305/shopping-list";
// сериализация, вместо:
//                        "{\n"
//                        + " \"item\": \"РусскаяКухня\",\n"
//                        + " \"aisle\": \"КартошкаССеледкой\",\n"
//                        + " \"parse\": true \n"
//                        + "}")
        NewDishRequest request = new NewDishRequest();
        request.setItem("РусскаяКухня");
        request.setAisle("КартошкаССеледкой");
        request.setParse(true);

        String idCreate = given()
                .queryParam("hash", gethash())
                .body(request)
                .when()
                .post(urlChain + "/items")
                .then()
                .extract()
                .jsonPath()
                .get("id")
                .toString();
        System.out.println("1й шаг: Создано блюдо с id= " + idCreate);

        String idDish = given()
                .when()
                .get(urlChain + "?hash=" + gethash())
                .then()
                .extract()
                .response()
                .body()
                .jsonPath()
// десериализацию  делать показалось излишним - много лишнего кода
                .get("aisles.items[0].id[0]")
                .toString();
        System.out.println("2й шаг: Есть ли блюдо с созданным id? "+ idCreate.equals(idDish));

        given()
                .queryParam("hash", gethash())
                .delete(urlChain + "/items/" + idCreate);
        System.out.println("3й шаг: Успешно удалено блюдо с id= " + idDish);
    }
}