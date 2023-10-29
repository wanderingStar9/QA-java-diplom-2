import client.UserClient;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserChangeTest extends BaseUserTest {
    private final String modifiedName = "Norman";
    private final String modifiedEmail = "Reedus@ya.ru";
    private final String modifiedPassword = "IamNorman1969";
    User changedUser = new User();
    private String accessToken;

    @Override
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://stellarburgers.nomoreparties.site";
        email = "samuraj999@ya.ru";
        password = "qwerty1234";
        name = "Simon";
        user = new User();
        userClient = new UserClient();
        user = new User(email, password, name);
        UserClient.postCreateNewUser(user);
        accessToken = UserClient.checkRequestUserLogin(user).then().extract().path("accessToken");
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Успешное изменение имени пользователя с авторизацией")
    public void changeUserNameWithAuthorizationTest() {
        changedUser.setName(modifiedName);
        user.setName(modifiedName);
        Response response = userClient.sendPatchRequestWithAuthorization(changedUser, accessToken);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Успешное изменение email пользователя с авторизацией")
    public void changeUserEmailWithAuthorizationTest() {
        changedUser.setEmail(modifiedEmail);
        user.setEmail(modifiedEmail);
        Response response = userClient.sendPatchRequestWithAuthorization(changedUser, accessToken);
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Изменение пароля пользователя с авторизацией")
    @Description("Успешное изменение пароля пользователя с авторизацией")
    public void changeUserPasswordWithAuthorizationTest() {
        changedUser.setPassword(modifiedPassword);
        user.setPassword(modifiedPassword);
        Response response = userClient.sendPatchRequestWithAuthorization(changedUser, accessToken);
        userClient.checkSuccessResponseChangeUser(response, email, name);
    }

    @Test
    @DisplayName("Изменение имени пользователя без авторизации")
    @Description("Неуспешное изменение имени пользователя без авторизации")
    public void changeUserNameWithoutAuthorizationTest() {
        changedUser.setName(modifiedName);
        user.setName(modifiedName);
        Response response = userClient.sendPatchRequestWithoutAuthorization(changedUser);
        userClient.checkFailedResponseChangeUser(response);
    }

    @Test
    @DisplayName("Изменение email пользователя без авторизации")
    @Description("Неуспешное изменение email пользователя без авторизации")
    public void changeUserEmailWithoutAuthorizationTest() {
        changedUser.setEmail(modifiedEmail);
        user.setEmail(modifiedEmail);
        Response response = userClient.sendPatchRequestWithoutAuthorization(changedUser);
        userClient.checkFailedResponseChangeUser(response);
    }

    @Test
    @DisplayName("Изменение пароля пользователя без авторизации")
    @Description("Неуспешное изменение пароля пользователя без авторизации")
    public void changeUserPasswordWithoutAuthorizationTest() {
        changedUser.setPassword(modifiedPassword);
        user.setPassword(modifiedPassword);
        Response response = userClient.sendPatchRequestWithoutAuthorization(changedUser);
        userClient.checkFailedResponseChangeUser(response);
    }

    @Override
    @After
    public void tearDown() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }
    }
}
