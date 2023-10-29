import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class OrderCreateTest {
    private String email;
    private String password;
    private String name;
    private UserClient userClient;
    private User user;
    private String accessToken;
    private OrderClient orderClient;
    private List<String> ingredient;
    private Order order;


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://stellarburgers.nomoreparties.site";
        email = "samuraj999@ya.ru";
        password = "qwerty1234";
        name = "Simon";
        userClient = new UserClient();
        orderClient = new OrderClient();
        user = new User(email, password, name);
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизациией и ингредиентами")
    @Description("Успешное создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        ingredient.add(ingredients.getData().get(4).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(7).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().body("order.ingredients", Matchers.notNullValue())
                .and().body("order._id", Matchers.notNullValue())
                .and().body("order.owner.name", Matchers.is(name))
                .and().body("order.owner.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                .and().body("order.status", Matchers.is("done"))
                .and().body("order.name", Matchers.notNullValue())
                .and().body("order.price", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    @Description("Успешное создание заказа без авторизации")
    public void createOrderWithoutAuthorizationTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        Response response = OrderClient.createOrderWithoutAuthorization(order);
        response.then().log().all()
                .assertThat().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().statusCode(200);
    }


    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    @Description("Проверка создания заказа с авторизацией без ингредиентов")
    public void createEmptyOrderWithAuthorization() {
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderClient.checkFailedResponseForOrderWithoutIngredients(response);
    }


    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    @Description("Проверка созданияgi заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id() + "0");
        ingredient.add(ingredients.getData().get(2).get_id() + "0");
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderClient.checkFailedResponseForOrderWithInvalidIngredientHashCode(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
