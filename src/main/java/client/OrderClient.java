package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;
import org.hamcrest.Matchers;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDER_ENDPOINT = "/api/orders";

    @Step("Получение данных об ингредиентах")
    public Ingredients getIngredient() {
        return given()
                .header("Content-Type", "application/json")
                .log().all()
                .get("/api/ingredients")
                .body()
                .as(Ingredients.class);
    }
    @Step("Создание заказа c авторизацией, с ингредиентами")
    public static Response createOrderWithAuthorization(Order order, String token) {
        return given().log().all().filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .header("authorization", token)
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAuthorization(Order order) {
        return given().log().all()
                .filter(new AllureRestAssured())
                .header("Content-Type", "application/json")
                .body(order)
                .when()
                .post(ORDER_ENDPOINT);
    }


    @Step("Создание заказа без ингредиентов")
    public void checkFailedResponseForOrderWithoutIngredients(Response response) {
        response.then().log().all()
                .assertThat().statusCode(400).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }


    @Step("Создание заказа с неверным хэшем ингредиентов")
    public void checkFailedResponseForOrderWithInvalidIngredientHashCode(Response response) {
        response.then().log().all()
                .assertThat().statusCode(500);
    }

    @Step("Получение списка заказов авторизованным пользователем")
    public static Response getUserOrderListWithAuthorization(String token) {
        return given().log().all()
                .header("Content-Type", "application/json")
                .header("authorization", token)
                .when()
                .get(ORDER_ENDPOINT);
    }

    @Step("Получение списка заказов без авторизации")
    public static Response getUserOrderListWithoutAuthorization() {
        return given().log().all()
                .header("Content-Type", "application/json")
                .when()
                .get(ORDER_ENDPOINT);
    }

    @Step("Получение списка заказов авторизованным пользователем")
    public static void checkSuccessfulResponseForGetUserOrdersWithAuthorization(Response response) {
        response.then().log().all()
                .assertThat().statusCode(200).and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }

    @Step("Получение списка заказов без авторизации")
    public static void checkFailedResponseForGetUserOrdersWithoutAuthorization(Response response) {
        response.then().log().all()
                .assertThat().statusCode(401).and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }

}
