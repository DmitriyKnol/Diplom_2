import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import main.pojo.AccessTokenBearer;
import main.pojo.CreateUser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class GetOrderUserTest {
    AccessTokenBearer accessTokenBearer;
    Faker faker = new Faker();
    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6,10);
    private final String userNameFaker = faker.name().firstName();
    private static final String handlerGetOrder = "/api/orders";

    CreateUser createUser = new CreateUser(emailFaker, passwordFaker, userNameFaker);

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUser)
                .when()
                .post("/api/auth/register")
                .then()
                .log().body()
                .extract().body().as(AccessTokenBearer.class);
    }
    @Test
    @DisplayName("Получение списка заказов пользователя с авторизацией")
    public void getUserOrdersWithAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Получение списка заказов пользователя без авторизации")
    public void getUserOrdersWithNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @After
    public void deleteUser() {
        if (accessTokenBearer.getAccessToken() != null) {
            given()
                    .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .log().body();
        }
    }
}
