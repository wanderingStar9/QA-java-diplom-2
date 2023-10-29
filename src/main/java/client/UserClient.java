package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class UserClient {
    private static final String USER_REGISTRATION_ENDPOINT = "/api/auth/register";
    private static final String USER_LOGIN_ENDPOINT = "/api/auth/login";
    private static final String USER_CHANGE_ENDPOINT = "/api/auth/user";

    @Step("Успешное создание уникального пользователя")
    public static Response postCreateNewUser(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_REGISTRATION_ENDPOINT);
    }

    @Step("Неуспешный ответ сервера на регистрацию пользователя, который уже зарегистрирован")
    public void checkFailedResponseRegisterDuplicateUser(Response response) {
        response.then().log().all()
                .assertThat().statusCode(403).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Step("Неуспешный ответ сервера на регистрацию пользователя без обязательных полей")
    public void checkFailedResponseRegisterUserWithoutRequiredFields(Response response) {
        response.then().log().all()
                .assertThat().statusCode(403).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Step("Логин под существующим пользователем")
    public static Response checkRequestUserLogin(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_LOGIN_ENDPOINT);
    }

    @Step("Логин с неверным логином и паролем.")
    public void checkFailedResponseUserLogin(Response response) {
        response.then().log().all()
                .assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @Step("Изменение данных пользователя с авторизацией")
    public Response sendPatchRequestWithAuthorization(User user, String token) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .header("authorization", token)
                .body(user)
                .when()
                .patch(USER_CHANGE_ENDPOINT);
    }

    @Step("Изменение данных пользователя без авторизации.")
    public Response sendPatchRequestWithoutAuthorization(User user) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .patch(USER_CHANGE_ENDPOINT);
    }

    @Step("Успешный ответ сервера на изменение данных пользователя.")
    public void checkSuccessResponseChangeUser(Response response, String email, String name) {
        response.then().log().all()
                .assertThat()
                .statusCode(200)
                .body("success", Matchers.is(true))
                .and().body("user.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                .and().body("user.name", Matchers.is(name));
    }

    @Step("Неуспешный ответ сервера на изменение данных пользователя.")
    public void checkFailedResponseChangeUser(Response response) {
        response.then().log().all()
                .assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }


    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_CHANGE_ENDPOINT);
    }
}
