import client.OrderClient;
import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

public class OrderGetTest extends BaseUserTest {
    private final User user = User.createRandomUser();

    @Test
    @DisplayName("Получение списка заказов авторизованным пользователем")
    @Description("Успешная проверка получения списка заказов авторизованного пользователя")
    public void getUserOrderWithAuthorizationTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.getToken(user);
        Response response = OrderClient.getUserOrderListWithAuthorization(accessToken);
        orderResponseCheckClient.checkSuccessfulResponseForGetUserOrdersWithAuthorization(response);
    }

    @Test
    @DisplayName("Получение списка заказов без авторизации")
    @Description("Неуспешная проверка получение списка заказов без авторизации")
    public void getUserOrderWithoutAuthorizationTest() {
        Response response = OrderClient.getUserOrderListWithoutAuthorization();
        orderResponseCheckClient.checkFailedResponseForGetUserOrdersWithoutAuthorization(response);
    }

}
