package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.Locale;

import static org.apache.http.HttpStatus.*;

public class UserResponseCheckClient {

    @Step("Успешная регистрация пользователя")
    public static void checkSuccessResponseUserRegistration(Response response) {
        response.then().log().all().assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.notNullValue())
                .and().body("user.name", Matchers.notNullValue());
    }

    @Step("Логин с неверным логином и паролем")
    public void checkFailedResponseUserLogin(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_UNAUTHORIZED).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("email or password are incorrect"));
    }

    @Step("Успешный ответ сервера на изменение данных пользователя.")
    public void checkSuccessResponseChangeUser(Response response, String email, String name) {
        response.then().log().all()
                .assertThat()
                .statusCode(SC_OK)
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                .and().body("user.name", Matchers.is(name));
    }

    @Step("Неуспешный ответ сервера на изменение данных пользователя.")
    public void checkFailedResponseChangeUser(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_UNAUTHORIZED).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @Step("Неуспешный ответ сервера на регистрацию пользователя, который уже зарегистрирован")
    public void checkFailedResponseRegisterDuplicateUser(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_FORBIDDEN).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }

    @Step("Неуспешный ответ сервера на регистрацию пользователя без обязательных полей")
    public void checkFailedResponseRegisterUserWithoutRequiredFields(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_FORBIDDEN).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }

    @Step("Успешный ответ при удалении пользователя")
    public void checkSuccessResponseDeleteUser(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_ACCEPTED);
    }
}
