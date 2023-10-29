import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.Test;

public class UserLoginTest extends BaseUserTest {
    @Test
    @DisplayName("Авторизация пользователя")
    @Description("Авторизация пользователя под существующем логином")
    public void authorizationTest() {
        user = new User(email, password, name);
        UserClient.postCreateNewUser(user);
        Response response = UserClient.checkRequestUserLogin(user);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Авторизация пользователя c некорректным логином")
    public void authorizationIncorrectLoginTest() {
        user = new User(email, password);
        user.setEmail("Alkjd2000" + email);
        Response response = UserClient.checkRequestUserLogin(user);
        userClient.checkFailedResponseUserLogin(response);
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Авторизация пользователя c некорректным паролем")
    public void authorizationIncorrectPasswordTest() {
        user = new User(email, password);
        user.setPassword("dljfgh" + password);
        Response response = UserClient.checkRequestUserLogin(user);
        userClient.checkFailedResponseUserLogin(response);
    }

    @Test
    @DisplayName("Авторизация без логина и пароля")
    @Description("Авторизация пользователя без логина и пароля")
    public void authorizationWithoutLoginAndPasswordTest() {
        Response response = UserClient.checkRequestUserLogin(user);
        userClient.checkFailedResponseUserLogin(response);
    }
}
