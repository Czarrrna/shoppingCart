import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testData.TestUtils;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class ShoppingCartAPIErrors {
    String payload;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @BeforeEach
    public void setupData() throws IOException {
        String payload = TestUtils.readJson("src/test/resources/cart.json");
    }

    @Test
    void shouldReturnInternalServerError() {

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order?500")
                .then()
                .statusCode(500)
                .body("error", containsString("Internal Server Error"));
    }

    @Test
    void shouldReturnServiceUnavailableError() {
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order?503")
                .then()
                .statusCode(503)
                .body("error", containsString("Service Unavailable"));
    }

    @Test
    void shouldReturnGatewayTimeoutError() {
        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order?504")
                .then()
                .statusCode(504)
                .body("error", containsString("Gateway Timeout"));
    }
}
