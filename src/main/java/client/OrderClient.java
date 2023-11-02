package client;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import model.Ingredients;
import model.Order;

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

}
