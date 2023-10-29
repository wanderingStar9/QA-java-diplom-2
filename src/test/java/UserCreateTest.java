import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.Test;

public class UserCreateTest extends BaseUserTest {
    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Регистрация уникального пользователя c корректными данными")
    public void checkCreateUserTest() {
        user = new User(email, password, name);
        Response response = UserClient.postCreateNewUser(user);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Регистрация уже зарегистрированного пользователя")
    public void checkRegisteredUserTest() {
        user = new User(email, password, name);
        UserClient.postCreateNewUser(user);
        Response response = UserClient.postCreateNewUser(user);
        userClient.checkFailedResponseRegisterDuplicateUser(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    @Description("Регистрация пользователя без имени, но с заполненными email и password")
    public void createUserWithoutNameTest() {
        user.setEmail(email);
        user.setPassword(password);
        Response response = UserClient.postCreateNewUser(user);
        userClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без email")
    @Description("Регистрация пользователя без email, но с заполненными именем и паролем")
    public void createUserWithoutEmailTest() {
        user.setName(name);
        user.setPassword(password);
        Response response = UserClient.postCreateNewUser(user);
        userClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    @Description("Регистрация пользователя без пароля, но с заполненными именем и email.")
    public void createUserWithoutPasswordTest() {
        user.setEmail(email);
        user.setName(name);
        Response response = UserClient.postCreateNewUser(user);
        userClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }
}
