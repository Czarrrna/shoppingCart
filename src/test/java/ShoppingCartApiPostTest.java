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
    void shouldCreateCartWithValidCartDataAndReturnCorrectGrandTotalPrice() {
        BigDecimal expectedTotal = new BigDecimal(81.97);

        given()
                .contentType(ContentType.JSON)
                .body(payload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(201)
                .body("grandTotal", equalTo(expectedTotal))
                .body("currency", equalTo("GBP"))
                .body("message", equalTo("Successfully created."));
    }

    @Test
    void shouldRejectCartWithInvalidCartId() throws Exception {
        String invalidCartIdPayload = TestUtils.overrideField(payload, "cartId","abcde-00000");

        given()
                .contentType(ContentType.JSON)
                .body(invalidCartIdPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("cartId is incorrect"));

    }

    @Test
    void shouldRejectCartWithInvalidCartIdFormat() throws Exception {
        String invalidCartIdFormatPayload = TestUtils.overrideField(payload, "cartId","0001");

        given()
                .contentType(ContentType.JSON)
                .body(invalidCartIdFormatPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("cartId format is incorrect"));

    }

    @Test
    void shouldRejectCartWithInvalidUserId() throws Exception {
        String invalidUserIdPayload = TestUtils.overrideField(payload, "userId", "99999");

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
    void shouldRejectCartWithInvalidProductId() throws Exception {
        String invalidProductIdPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "productId", "SKU-999");

        given()
                .contentType(ContentType.JSON)
                .body(invalidProductIdPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("invalid product id"));
    }

    @Test
    void shouldRejectCartWithInvalidProductIdFormat() throws Exception {
        String invalidProductIdFormatPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "productId", "SKU01");

        given()
                .contentType(ContentType.JSON)
                .body(invalidProductIdFormatPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("invalid product id format"));
    }

    @Test
    void shouldRejectCartWithInvalidProductName() throws Exception {
        String invalidProductNamePayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "productName", "Monitor");

        given()
                .contentType(ContentType.JSON)
                .body(invalidProductNamePayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("invalid product name"));
    }

    @Test
    void shouldRejectCartWithInvalidCurrency() throws Exception {
        String invalidCurrencyPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "currency", "XYZ");

        given()
                .contentType(ContentType.JSON)
                .body(invalidCurrencyPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("currency is incorrect"));
    }

    @Test
    void shouldRejectCartWithTwoDifferentCurrencies() throws Exception {
        String invalidCurrencyPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "currency", "USD");

        given()
                .contentType(ContentType.JSON)
                .body(invalidCurrencyPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("currency is inconsistent"));
    }

    @Test
    void shouldRejectCartWithZeroQuantity() throws Exception {
        String zeroQuantityPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "quantity", 0);

        given()
                .contentType(ContentType.JSON)
                .body(zeroQuantityPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("quantity is zero"));
    }

    @Test
    void shouldRejectCartWithTooBigQuantity() throws Exception {
        String tooBigQuantityPayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "quantity", 1000);

        given()
                .contentType(ContentType.JSON)
                .body(tooBigQuantityPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("quantity is too big"));
    }

    @Test
    void shouldRejectCartWithZeroPrice() throws Exception {
        String zeroPricePayload = TestUtils.overrideNestedFieldAtIndex(payload, "items", 0, "price", 0);

        given()
                .contentType(ContentType.JSON)
                .body(zeroPricePayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("currency is incorrect"));
    }

    @Test
    void shouldRejectCartWithTimestampInFuture() throws Exception {
        String invalidTimestampPayload = TestUtils.overrideField(payload, "timestamp", "2025-12-12T08:45:00Z");

        given()
                .contentType(ContentType.JSON)
                .body(invalidTimestampPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("timestamp is incorrect"));
    }

    @Test
    void shouldRejectCartWithTimestampFarInPast() throws Exception {
        String invalidTimestampPayload = TestUtils.overrideField(payload, "timestamp", "2000-09-12T08:45:00Z");

        given()
                .contentType(ContentType.JSON)
                .body(invalidTimestampPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", equalTo("timestamp is incorrect"));
    }


    @Test
    void shouldRejectCartWithMissingCartId() throws Exception {
        String missingCartIdPayload = TestUtils.removeField(payload, "cartId");

        given()
                .contentType(ContentType.JSON)
                .body(missingCartIdPayload)
                .when()
                .post("/shoppingcart/order")
                .then()
                .statusCode(400)
                .body("error", containsString("cartId is required"));
    }

    @Test
    void shouldRejectCartWithMissingUserId() throws Exception {
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
    void shouldRejectCartWithMissingItems() throws Exception {
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
