import client.UserClient;
import io.restassured.RestAssured;
import model.User;
import org.junit.After;
import org.junit.Before;

public class BaseUserTest {
    protected String email;
    protected String password;
    protected String name;
    protected User user;
    protected UserClient userClient;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://stellarburgers.nomoreparties.site";
        email = "samuraj999@ya.ru";
        password = "qwerty1234";
        name = "Simon";
        user = new User();
        userClient = new UserClient();
    }

    @After
    public void tearDown() {
        String accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        if (accessToken !=null) {
            userClient.deleteUser(accessToken);
        }
    }
}
