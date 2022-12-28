import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class RenameUserTest {
    AccessTokenBearer accessTokenBearer;

    Faker faker = new Faker();
    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6,10);
    private final String userNameFaker = faker.name().firstName();
    private final String newEmail = faker.internet().emailAddress();
    private final String newName = faker.name().firstName();
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
    @DisplayName("Проверка возможности изменения Email авторизованного пользователя")
    public void renameEmailWithAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"name\": \"" + newEmail + "\"}")
                .when()
                .patch("/api/auth/user")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
    @Test
    @DisplayName("Проверка возможности изменения имени авторизованного пользователя")
    public void renameNameWithAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"name\": \"" + newName + "\"}")
                .when()
                .patch("/api/auth/user")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }
    @Test
    @DisplayName("Проверка возможности изменения Email и имени авторизованного пользователя")
    public void renameEmailAndNameWithAuthorization() {
        given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"email\": \"" + newEmail + "\", \"name\": \"" + newName + "\"}")
                .when()
                .patch("/api/auth/user")
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Проверка возможности изменения Email неавторизованного пользователя")
    public void renameEmailNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"name\": \"" + newEmail + "\"}")
                .when()
                .patch("/api/auth/user")
                .then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Проверка возможности изменения имени неавторизованного пользователя")
    public void renameNameNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"name\": \"" + newName + "\"}")
                .when()
                .patch("/api/auth/user")
                .then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Проверка возможности изменения Email и имени неавторизованного пользователя")
    public void renameEmailAndNameNonAuthorization() {
        given()
                .header("Content-type", "application/json")
                .log().body()
                .body("{\"email\": \"" + newEmail + "\", \"name\": \"" + newName + "\"}")
                .when()
                .patch("/api/auth/user")
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
