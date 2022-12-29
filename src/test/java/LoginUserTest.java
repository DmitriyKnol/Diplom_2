import com.github.javafaker.Faker;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import main.pojo.AccessTokenBearer;
import main.pojo.CreateUser;
import main.pojo.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

public class LoginUserTest {
    AccessTokenBearer accessTokenBearer;
    AccessTokenBearer tokenBearer;
    Faker faker = new Faker();
    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6,10);
    private final String userNameFaker = faker.name().firstName();
    CreateUser createUser = new CreateUser(emailFaker, passwordFaker, userNameFaker);
    User user = new User(emailFaker, passwordFaker);
    User userBadEmail = new User(emailFaker.substring(1), passwordFaker);
    User userBadPassword = new User(emailFaker, passwordFaker.substring(1));
    User userNoEmail = new User("", passwordFaker);
    User userNoPassword = new User(emailFaker, "");


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
    @DisplayName("Проверка возможности успешного логина")
    public void successLoginUser(){
        accessTokenBearer = given()
            .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
            .header("Content-type", "application/json")
            .log().body()
            .body(user)
            .when()
            .post("/api/auth/login")
            .then()
            .log().body()
            .assertThat()
            .statusCode(200)
            .body("success", is(true))
            .extract().body().as(AccessTokenBearer.class);
}
    @Test
    @DisplayName("Проверка возможности логина c неправильным Email")
    public void loginUserWithBadEmail(){
        Response response = given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(userBadEmail)
                .when()
                .post("/api/auth/login");

        tokenBearer = response.then().extract().body().as(AccessTokenBearer.class);
        if (tokenBearer.getAccessToken() != null) {
            accessTokenBearer = tokenBearer;
        }
        response.then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Проверка возможности логина c неправильным паролем")
    public void loginUserWithBadPassword(){
        Response response = given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(userBadPassword)
                .when()
                .post("/api/auth/login");

        tokenBearer = response.then().extract().body().as(AccessTokenBearer.class);
        if (tokenBearer.getAccessToken() != null) {
            accessTokenBearer = tokenBearer;
        }
        response.then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Проверка возможности логина без Email")
    public void loginUserWithoutEmail(){
        Response response = given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(userNoEmail)
                .when()
                .post("/api/auth/login");

        tokenBearer = response.then().extract().body().as(AccessTokenBearer.class);
        if (tokenBearer.getAccessToken() != null) {
            accessTokenBearer = tokenBearer;
        }
        response.then()
                .log().body()
                .assertThat()
                .statusCode(401)
                .body("success", is(false));
    }
    @Test
    @DisplayName("Проверка возможности логина без пароля")
    public void loginUserWithoutPassword(){
        Response response = given()
                .auth().oauth2(accessTokenBearer.getAccessToken().substring(7))
                .header("Content-type", "application/json")
                .log().body()
                .body(userNoPassword)
                .when()
                .post("/api/auth/login");

        tokenBearer = response.then().extract().body().as(AccessTokenBearer.class);
        if (tokenBearer.getAccessToken() != null) {
            accessTokenBearer = tokenBearer;
        }
        response.then()
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
