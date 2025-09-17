import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ShoppingCartAPIErrors {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }


    @Test
    void testInternalServerError() {
        String brokenPayload = "{ \"userID\": \"12345\", \"cartId\": null }";

        given()
                .contentType(ContentType.JSON)
                .body(brokenPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(500)
                .body("error", containsString("Internal Server Error"));
    }

    @Test
    void testServiceUnavailable() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(503)
                .body("error", containsString("Service Unavailable"));
    }

    @Test
    void testGatewayTimeout() {
        given()
                .contentType(ContentType.JSON)
                .body("{}")
                .when()
                .post("/shoppingcart/order?simulate=timeout")
                .then()
                .statusCode(504)
                .body("error", containsString("Gateway Timeout"));
    }


}
