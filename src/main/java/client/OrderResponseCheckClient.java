package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.Matchers;

import java.util.Locale;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;

public class OrderResponseCheckClient {

    @Step("Создание заказа без ингредиентов")
    public void checkFailedResponseForOrderWithoutIngredients(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_BAD_REQUEST).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }

    @Step("Создание заказа с неверным хэшем ингредиентов")
    public void checkFailedResponseForOrderWithInvalidIngredientHashCode(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Успешное создание заказа авторизованным пользователем")
    public static void checkSuccessResponseForOrderWithAuthorization(Response response, String name, String email) {
        response.then().log().all()
                .assertThat().statusCode(SC_OK).and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().body("order.ingredients", Matchers.notNullValue())
                .and().body("order._id", Matchers.notNullValue())
                .and().body("order.owner.name", Matchers.is(name))
                .and().body("order.owner.email", Matchers.is(email.toLowerCase(Locale.ROOT)))
                .and().body("order.status", Matchers.is("done"))
                .and().body("order.name", Matchers.notNullValue())
                .and().body("order.price", Matchers.notNullValue());
    }

    @Step("Создание заказа без авторизации")
    public static void checkReponseForOrderWithoutAuthorization(Response response) {
        response.then().log().all()
                .assertThat().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class))
                .and().statusCode(SC_OK);
    }

    @Step("Получение списка заказов авторизованным пользователем")
    public static void checkSuccessfulResponseForGetUserOrdersWithAuthorization(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_OK).and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }

    @Step("Получение списка заказов без авторизации")
    public static void checkFailedResponseForGetUserOrdersWithoutAuthorization(Response response) {
        response.then().log().all()
                .assertThat().statusCode(SC_UNAUTHORIZED).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
}
