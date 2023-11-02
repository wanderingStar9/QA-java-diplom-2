import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.junit.Test;

public class UserChangeTest extends BaseUserTest {
    private final User user = User.createRandomUser();


    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Успешное изменение имени пользователя с авторизацией")
    public void changeUserNameWithAuthorizationTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        String newName = User.createRandomUserData();
        User user = new User(this.user.getEmail(), this.user.getPassword(), newName);
        Response response = userClient.sendPatchRequestWithAuthorization(user, accessToken);
        userResponseCheckClient.checkSuccessResponseChangeUser(response, user.getEmail(), user.getName());
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Успешное изменение email пользователя с авторизацией")
    public void changeUserEmailWithAuthorizationTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        String newEmail = User.createRandomEmail();
        User user = new User(newEmail, this.user.getPassword(), this.user.getName());
        Response response = userClient.sendPatchRequestWithAuthorization(user, accessToken);
        userResponseCheckClient.checkSuccessResponseChangeUser(response, user.getEmail(), user.getName());
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Успешное изменение пароля пользователя с авторизацией")
    public void changeUserPasswordWithAuthorizationTest() {
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
        String newPassword = User.createRandomUserData();
        User user = new User(this.user.getEmail(), newPassword, this.user.getName());
        Response response = userClient.sendPatchRequestWithAuthorization(user, accessToken);
        userResponseCheckClient.checkSuccessResponseChangeUser(response, user.getEmail(), user.getName());
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Неуспешное изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorizationTest() {
        Response response = userClient.sendPatchRequestWithoutAuthorization(user);
        userResponseCheckClient.checkFailedResponseChangeUser(response);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Неуспешное изменение email пользователя без авторизации")
    public void changeUserEmailWithoutAuthorizationTest() {
        Response response = userClient.sendPatchRequestWithoutAuthorization(user);
        userResponseCheckClient.checkFailedResponseChangeUser(response);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Неуспешное изменение пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuthorizationTest() {
        Response response = userClient.sendPatchRequestWithoutAuthorization(user);
        userResponseCheckClient.checkFailedResponseChangeUser(response);
    }

}
