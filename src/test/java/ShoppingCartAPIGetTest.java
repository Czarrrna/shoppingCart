import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class ShoppingCartAPIGetTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8080/api";
    }

    @Test
    void shouldGetCartByIdSuccess() {
        String cartId = "abcde-67890";

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/shoppingcart/{cartId}", cartId)
                .then()
                .statusCode(200)
                .body("cartId", equalTo(cartId))
                .body("userID", equalTo("12345"))
                .body("items.size()", equalTo(2))
                .body("items[0].productId", equalTo("SKU-001"))
                .body("items[0].productName", equalTo("Wireless Mouse"))
                .body("items[0].quantity", equalTo(2))
                .body("items[0].price", equalTo(15.99))
                .body("items[0].currency", equalTo("GBP"))
                .body("items[1].productId", equalTo("SKU-002"))
                .body("items[1].productName", equalTo("Wireless Keyboard"))
                .body("items[1].quantity", equalTo(1))
                .body("items[1].price", equalTo(49.99))
                .body("items[1].currency", equalTo("GBP"));
    }

    @Test
    void shouldRejectGettingCartByIdNotFound() {
        String invalidCartId = "abcde-99999";

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/shoppingcart/{cartId}", invalidCartId)
                .then()
                .statusCode(404)
                .body("error", equalTo("Cart not found"));
    }

    @Test
    void shouldRejectGettingCartByIdInvalidFormat() {
        String invalidFormatCartId = "123";

        given()
                .contentType(ContentType.JSON)
                .when()
                .get("/shoppingcart/{cartId}", invalidFormatCartId)
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid cartId format"));
    }
}
