import client.OrderResponseCheckClient;
import client.UserClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.junit.After;
import org.junit.Before;
import client.UserResponseCheckClient;

public class BaseUserTest {
    protected User user;
    protected UserClient userClient;
    protected UserResponseCheckClient userResponseCheckClient;
    protected OrderResponseCheckClient orderResponseCheckClient;
    protected String accessToken;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://stellarburgers.nomoreparties.site";
        user = new User();
        userClient = new UserClient();
        userResponseCheckClient = new UserResponseCheckClient();
        orderResponseCheckClient = new OrderResponseCheckClient();
    }

    @After
    public void tearDown() {
        if (userClient.getToken(user) !=null) {
            Response response = userClient.deleteUser(UserClient.getToken(user));
            userResponseCheckClient.checkSuccessResponseDeleteUser(response);
        }
    }
}
