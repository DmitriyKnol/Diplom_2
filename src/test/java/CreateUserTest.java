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

public class CreateUserTest {
    AccessTokenBearer accessTokenBearer;
    Faker faker = new Faker();
    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6,10);
    private final String userNameFaker = faker.name().firstName();
    private static final String handlerRegister = "/api/auth/register";
    CreateUser createUser = new CreateUser(emailFaker, passwordFaker, userNameFaker);
    CreateUser createUserWithoutEmail = new CreateUser("", passwordFaker, userNameFaker);
    CreateUser createUserWithoutPassword = new CreateUser(emailFaker, "", userNameFaker);
    CreateUser createUserWithoutUserName = new CreateUser(emailFaker, passwordFaker, "");

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test
    @DisplayName("Проверка возможности создать нового пользователя")
    public void successfulRegistrationNewUser() {
        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUser)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .assertThat()
                .statusCode(200)
                .body("success", is(true))
                .extract().body().as(AccessTokenBearer.class);

    }

    @Test
    @DisplayName("Попытка создать пользователя с повторяющимся именем")
    public void registrationDuplicateUser() {
        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUser)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .extract().body().as(AccessTokenBearer.class);

        given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUser)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .assertThat()
                .statusCode(403)
                .body("success", is(false));

    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного email")
    public void registrationUserWithoutEmail() {
        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUserWithoutEmail)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .extract().body().as(AccessTokenBearer.class);

    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного Password")
    public void registrationUserWithoutPassword() {
        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUserWithoutPassword)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .extract().body().as(AccessTokenBearer.class);

    }

    @Test
    @DisplayName("Попытка создания пользователя без заполненного имени пользователя")
    public void registrationUserWithoutUserName() {
        accessTokenBearer = given()
                .header("Content-type", "application/json")
                .log().body()
                .body(createUserWithoutUserName)
                .when()
                .post(handlerRegister)
                .then()
                .log().body()
                .assertThat()
                .statusCode(403)
                .body("success", is(false))
                .extract().body().as(AccessTokenBearer.class);

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