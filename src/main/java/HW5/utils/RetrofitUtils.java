package HW5.utils;

import lombok.experimental.UtilityClass;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@UtilityClass
public class RetrofitUtils {
    public String baseUrl="http://localhost:8189/market/api/v1/";
//    Properties prop = new Properties();
//    private static InputStream configFile;
//    static {
//        try {
//         configFile = new FileInputStream("src/main/resources/my.properties");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//    @SneakyThrows
//    public String getBaseUrl() {
//        prop.load(configFile);
//        return prop.getProperty("url");
//    }
//    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    public Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

    }
}