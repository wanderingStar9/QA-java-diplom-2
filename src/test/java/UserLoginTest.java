import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.Test;
import static org.apache.http.HttpStatus.SC_OK;

public class UserLoginTest extends BaseUserTest {
    private final User user = User.createRandomUser();

    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя под существующем логином")
    public void authorizationTest() {
        UserClient.postCreateNewUser(user);
        Response response = UserClient.checkRequestUserLogin(user);
        response.then().log().all().assertThat().statusCode(SC_OK).and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Авторизация пользователя c некорректным логином")
    public void authorizationIncorrectLoginTest() {
        user.setEmail("Alkjd2000" + user.getEmail());
        Response response = UserClient.checkRequestUserLogin(user);
        userResponseCheckClient.checkFailedResponseUserLogin(response);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Авторизация пользователя c некорректным паролем")
    public void authorizationIncorrectPasswordTest() {
        user.setPassword("dljfgh" + user.getPassword());
        Response response = UserClient.checkRequestUserLogin(user);
        userResponseCheckClient.checkFailedResponseUserLogin(response);
    }

    @Test
    @DisplayName("Авторизация без логина и пароля")
    @Description("Авторизация пользователя без логина и пароля")
    public void authorizationWithoutLoginAndPasswordTest() {
        Response response = UserClient.checkRequestUserLogin(new User("", ""));
        userResponseCheckClient.checkFailedResponseUserLogin(response);
    }
}
