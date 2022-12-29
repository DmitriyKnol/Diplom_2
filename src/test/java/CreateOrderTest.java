import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import main.pojo.AccessTokenBearer;
import main.pojo.CreateUser;
import main.pojo.Ingredients;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class CreateOrderTest {
    String ingredient1;
    String ingredient2;
    AccessTokenBearer accessTokenBearer;
    Ingredients ingredients = new Ingredients(List.of("nononohash", "nohashtoo"));
    Ingredients ingredientsValid;
    Faker faker = new Faker();


    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6,10);
    private final String userNameFaker = faker.name().firstName();

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
        Response response = given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/ingredients");

        ingredient1 = response.then()
                .extract().jsonPath().getString("data[0]._id");
        ingredient2 = response.then()
                .extract().jsonPath().getString("data[1]._id");
        ingredientsValid = new Ingredients(List.of(ingredient1, ingredient2));
    }
    @Test
    @DisplayName("Создание заказа авторизованного пользователя")
    public void createOrderAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(ingredientsValid)
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа авторизованного пользователя без ингредиентов")
    public void createOrderWithAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(400)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Создание заказа авторизованного пользователя с невалидным хэшем")
    public void createOrderWithBadHashAndAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа неавторизованного пользователя")
    public void createOrderNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .log().body()
                .body(ingredientsValid)
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
    @Test
    @DisplayName("Создание заказа неавторизованного пользователя без ингредиентов")
    public void createOrderWithNonAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(400)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Создание заказа неавторизованного пользователя с невалидным хэшем")
    public void createOrderWithBadHashAndNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .log().body()
                .body(ingredients)
                .when()
                .post("/api/orders")
                .then()
                .log().body()
                .assertThat()
                .statusCode(500);
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
