package HW5;

// 02/09/2022, Yuriy Lomtev, HW5
// 5. Автоматизированное тестирование REST API c использованием Retrofit/OkHttp3
//		1. Напишите автотесты для CRUD-запросов сервиса продуктов, используя retrofit.
//		2. В README приложите чек-лист или майнд-карту того, что проверяют ваши тесты.
//		3. Сдайте ссылку на репозиторий, указав ветку с кодом.

import HW5.api.ProductService;
import HW5.dto.Product;
import HW5.utils.RetrofitUtils;
import com.github.javafaker.Faker;
import okhttp3.ResponseBody;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import retrofit2.Response;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ProductsTest {
    static ProductService productService;
    static Product product = null;
    static Faker faker = new Faker();

    @BeforeAll
    static void beforeAll() {
        productService = RetrofitUtils.getRetrofit().create(ProductService.class);
        product = new Product()
                .withTitle(faker.food().ingredient())
                .withCategoryTitle("Food")
                .withPrice((int) (Math.random() * 10000));
    }
    @Test
    void create_deleteProduct() throws IOException {
        Response<Product> response = productService.createProduct(product).execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
        Integer id=response.body().getId();
//        System.out.println("create_deleteProduct-"+response+"\n"+response.body().getId()+
//     "\t"+response.body().getTitle()+"\t"+response.body().getCategoryTitle()+"\t"+response.body().getPrice());
        Response<ResponseBody> response1 = productService.deleteProduct(id).execute();
        assertThat(response1.isSuccessful(), CoreMatchers.is(true));
    }
    @Test
    void modifyProduct() throws IOException {
        Response<Product> response = productService.modifyProduct(new Product(1,"Новой продукт",
                        123456,"Food")).execute();
        assertThat(response.body().getTitle(), equalTo("Новой продукт"));
//        System.out.println("Стало modifyProduct-"+response+"\n"+response.body().getId()+"\t"+
//           response.body().getTitle()+"\t"+response.body().getCategoryTitle()+"\t"+response.body().getPrice());
     }
    @Test
    void getProductById() throws IOException {
        Response<Product> response = productService.getProductById(1).execute();
        assertThat(response.body().getTitle(),
                anyOf(containsString("Новой продукт"), containsString("Milk")));
//        System.out.println("Было getProductById-"+response+"\n"+response.body().getId()+"\t"+
//           response.body().getTitle()+"\t"+response.body().getCategoryTitle()+"\t"+response.body().getPrice());
    }
    @Test
    void getProducts() throws IOException {
        Response<ResponseBody> response = productService.getProducts().execute();
        assertThat(response.isSuccessful(), CoreMatchers.is(true));
//        System.out.println("getProducts-"+response);
    }
}