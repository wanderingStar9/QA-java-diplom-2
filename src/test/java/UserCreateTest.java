import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

public class UserCreateTest extends BaseUserTest {
    private final User user = User.createRandomUser();
    @Test
    @DisplayName("Проверка создания уникального пользователя")
    @Description("Регистрация уникального пользователя c корректными данными")
    public void checkCreateUserTest() {
        Response response = UserClient.postCreateNewUser(user);
        userResponseCheckClient.checkSuccessResponseUserRegistration(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    @Description("Регистрация уже зарегистрированного пользователя")
    public void checkRegisteredUserTest() {
        UserClient.postCreateNewUser(user);
        Response response = UserClient.postCreateNewUser(user);
        userResponseCheckClient.checkFailedResponseRegisterDuplicateUser(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    @Description("Регистрация пользователя без имени, но с заполненными email и password")
    public void createUserWithoutNameTest() {
        Response response = UserClient.postCreateNewUser(new User(user.getEmail(), user.getPassword(), ""));
        userResponseCheckClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без email")
    @Description("Регистрация пользователя без email, но с заполненными именем и паролем")
    public void createUserWithoutEmailTest() {
        Response response = UserClient.postCreateNewUser(new User("", user.getPassword(), user.getName()));
        userResponseCheckClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    @Description("Регистрация пользователя без пароля, но с заполненными именем и email.")
    public void createUserWithoutPasswordTest() {
        Response response = UserClient.postCreateNewUser(new User(user.getEmail(), "", user.getName()));
        userResponseCheckClient.checkFailedResponseRegisterUserWithoutRequiredFields(response);
    }
}
