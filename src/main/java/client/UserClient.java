package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.User;

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


    @Step("Логин под существующим пользователем")
    public static Response checkRequestUserLogin(User user) {
        return given().log().all()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(USER_LOGIN_ENDPOINT);
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

    @Step("Получение токена")
    public static String getToken(User user){
        return checkRequestUserLogin(user).then().extract().path("accessToken");
    }

    @Step("Удаление пользователя")
    public Response deleteUser(String accessToken){
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(USER_CHANGE_ENDPOINT);
    }
}
