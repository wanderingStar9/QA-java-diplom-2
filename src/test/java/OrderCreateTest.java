import client.OrderClient;
import client.OrderResponseCheckClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class OrderCreateTest {
    private UserClient userClient;
    private String accessToken;
    private OrderClient orderClient;
    private List<String> ingredient;
    private Order order;
    private OrderResponseCheckClient orderResponseCheckClient;
    private final User user = User.createRandomUser();


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://stellarburgers.nomoreparties.site";
        userClient = new UserClient();
        orderClient = new OrderClient();
        orderResponseCheckClient = new OrderResponseCheckClient();
        ingredient = new ArrayList<>();
        order = new Order(ingredient);
    }

    @Test
    @DisplayName("Создание заказа с авторизациией и ингредиентами")
    @Description("Успешное создание заказа с авторизацией и ингредиентами")
    public void createOrderWithAuthorizationTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.getToken(user);
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id());
        ingredient.add(ingredients.getData().get(2).get_id());
        ingredient.add(ingredients.getData().get(3).get_id());
        ingredient.add(ingredients.getData().get(4).get_id());
        ingredient.add(ingredients.getData().get(5).get_id());
        ingredient.add(ingredients.getData().get(7).get_id());
        ingredient.add(ingredients.getData().get(8).get_id());
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        OrderResponseCheckClient.checkSuccessResponseForOrderWithAuthorization(response, user.getName(), user.getEmail());
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
        orderResponseCheckClient.checkReponseForOrderWithoutAuthorization(response);
    }


    @Test
    @DisplayName("Создание заказа с авторизацией без ингредиентов")
    @Description("Проверка создания заказа с авторизацией без ингредиентов")
    public void createEmptyOrderWithAuthorization() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.getToken(user);
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderResponseCheckClient.checkFailedResponseForOrderWithoutIngredients(response);
    }


    @Test
    @DisplayName("Создание заказа с авторизацией с неверным хешем ингредиентов")
    @Description("Проверка созданияgi заказа с авторизацией с неверным хешем ингредиентов")
    public void createOrderWithAuthorizationWithWrongHashTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.getToken(user);
        Ingredients ingredients = orderClient.getIngredient();
        ingredient.add(ingredients.getData().get(1).get_id() + "0");
        ingredient.add(ingredients.getData().get(2).get_id() + "0");
        Response response = OrderClient.createOrderWithAuthorization(order, accessToken);
        orderResponseCheckClient.checkFailedResponseForOrderWithInvalidIngredientHashCode(response);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
