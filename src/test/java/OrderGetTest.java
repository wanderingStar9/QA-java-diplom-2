import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Test;

public class OrderGetTest extends BaseUserTest {
    private String accessToken;

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Успешная проверка получения списка заказов авторизованного пользователя")
    public void getUserOrderWithAuthorizationTest() {
        user = new User(email, password, name);
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        Response response = OrderClient.getUserOrderListWithAuthorization(accessToken);
        OrderClient.checkSuccessfulResponseForGetUserOrdersWithAuthorization(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Неуспешная проверка получение списка заказов без авторизации")
    public void getUserOrderWithoutAuthorizationTest() {
        Response response = OrderClient.getUserOrderListWithoutAuthorization();
        OrderClient.checkFailedResponseForGetUserOrdersWithoutAuthorization(response);
    }

    @Override
    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }

}
