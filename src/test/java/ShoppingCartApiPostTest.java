import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testData.TestUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

public class ShoppingCartApiPostTest {
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
    void testCalculateTotalPrice() {
        BigDecimal expectedTotal = new BigDecimal(81.97);

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(201)
                .body("grandTotal", equalTo(expectedTotal))
                .body("currency", equalTo("GBP"));
    }


    @Test
    void testPostValidCartData() {

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(201)
                .body("message", equalTo("Successfully created."));
    }

    @Test
    void testPostInvalidCartId() throws Exception {
        String invalidCartId = TestUtils.overrideCartId(payload, "test-12345");

        given()
                .contentType(ContentType.JSON)
                .body(invalidCartId)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("cartId is incorrect"));

    }

    @Test
    void testPostInvalidUserId() throws Exception {
        String invalidUserIdPayload = TestUtils.overrideUserId(payload, "99999");

        given()
                .contentType(ContentType.JSON)
                .body(invalidUserIdPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("userId is incorrect"));
    }

    @Test
    void testMissingCartId() throws Exception {
        String missingCartId = TestUtils.removeField(payload, "cartId");
        given()
                .contentType(ContentType.JSON)
                .body(missingCartId)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", containsString("cartId is required"));
    }

    @Test
    void testMissingUserId() throws Exception {
        String missingUserId = TestUtils.removeField(payload, "userId");
        given()
                .contentType(ContentType.JSON)
                .body(missingUserId)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", containsString("userId is required"));
    }

    @Test
    void testMissingItems() throws Exception {
        String missingItems = TestUtils.removeField(payload, "items");
        given()
                .contentType(ContentType.JSON)
                .body(missingItems)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", containsString("Items are required"));
    }

}
