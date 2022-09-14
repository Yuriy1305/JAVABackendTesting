package HW6;
// 05.09.2022   Yuriy Lomtev    HW6
//Урок 6. ORM: почему JDBC иногда не достаточно
//        1. Встройте проверку данных через БД в ассерты к тестам с использованием MyBatis ORM.
//        2. Добавьте апдейт и удаление данных через БД в тестах и после них.
//        3. Сдайте ссылку на репозиторий и укажите ветки с кодом.

import HW6.db.dao.CategoriesMapper;
import HW6.db.model.Categories;
import HW6.db.model.CategoriesExample;
import lombok.SneakyThrows;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

public class TestsCategories {
    static SqlSession session = null;

    @BeforeAll
    static void beforeAll() {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        SqlSessionFactory sqlSessionFactory;
        {
            try {
                inputStream = Resources.getResourceAsStream(resource);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        }
        session = sqlSessionFactory.openSession();
    }
// Проверка Категории с id=1, должна быть Electronic
    @Test
    void getCategoryByIdPositiveTest() {
        Integer iD = 1;
        CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);
        CategoriesExample example = new CategoriesExample();
        List<Categories> list = categoriesMapper.selectByExample(example);

        Categories categories = list.get(iD);
        System.out.println("Тест1: Категория с id=" + iD + " - " + categories.getTitle());
        assertThat(categories.getTitle(), equalTo("Electronic"));
    }
// Проверка Категории с id=2, должна быть не Electronic
    @SneakyThrows
    @Test
    void getCategoryByIdNegativeTest() {
        int iD = 2;
        CategoriesMapper categoriesMapper = session.getMapper(CategoriesMapper.class);
        CategoriesExample example = new CategoriesExample();
        List<Categories> list = categoriesMapper.selectByExample(example);

        Categories categories = list.get(iD);
        System.out.println("Тест2: Категория с id=" + iD + " - " + categories.getTitle() + ", а не Electronic.");
        assertThat(categories.getTitle(), not("Electronic"));
    }
// Закрываем сессию с БД
    @AfterAll
    static void after() {
        {
            session.close();
        }
    }
}