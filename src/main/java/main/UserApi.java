package main;

import com.github.javafaker.Faker;
import io.restassured.response.Response;
import main.pojo.AccessTokenBearer;
import main.pojo.CreateUser;
import main.pojo.User;

import static io.restassured.RestAssured.given;

public class UserApi {
    AccessTokenBearer accessTokenBearer;
    Faker faker = new Faker();
    private static final String handlerRegister = "/api/auth/register";
    private static final String handlerLogin = "/api/auth/login";
    private static final String handlerUser = "/api/auth/user";
    private final String emailFaker = faker.internet().emailAddress();
    private final String passwordFaker = faker.internet().password(6, 10);
    private final String userNameFaker = faker.name().firstName();
    private final String newEmail = faker.internet().emailAddress();
    private final String newName = faker.name().firstName();
    CreateUser createUser = new CreateUser(emailFaker, passwordFaker, userNameFaker);
    CreateUser createUserWithoutEmail = new CreateUser("", passwordFaker, userNameFaker);
    CreateUser createUserWithoutPassword = new CreateUser(emailFaker, "", userNameFaker);
    CreateUser createUserWithoutUserName = new CreateUser(emailFaker, passwordFaker, "");
    User user = new User(emailFaker, passwordFaker);
    User userBadEmail = new User(emailFaker.substring(1), passwordFaker);
    User userBadPassword = new User(emailFaker, passwordFaker.substring(1));

    public String getNewEmail() {
        return newEmail;
    }

    public String getNewName() {
        return newName;
    }

    public CreateUser getCreateUser() {
        return createUser;
    }

    public CreateUser getCreateUserWithoutEmail() {
        return createUserWithoutEmail;
    }

    public CreateUser getCreateUserWithoutPassword() {
        return createUserWithoutPassword;
    }

    public CreateUser getCreateUserWithoutUserName() {
        return createUserWithoutUserName;
    }

    public User getUser() {
        return user;
    }

    public User getUserBadEmail() {
        return userBadEmail;
    }

    public User getUserBadPassword() {
        return userBadPassword;
    }

    public Response registration(CreateUser user) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .log().body()
                        .body(user)
                        .when()
                        .post(handlerRegister);
        accessTokenBearer = response
                .then()
                .log().body()
                .extract().body().as(AccessTokenBearer.class);

        return response;
    }

    public Response registrationWhithoutExtractToken(CreateUser user) {
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .log().body()
                        .body(user)
                        .when()
                        .post(handlerRegister);
        response
                .then()
                .log().body();
        return response;
    }

    public String getToken() {
        return accessTokenBearer.getAccessToken().substring(7);
    }

    public Response loginUser(User user) {
        Response response =
                given()
                        .auth().oauth2(getToken())
                        .header("Content-type", "application/json")
                        .log().body()
                        .body(user)
                        .when()
                        .post(handlerLogin);
        response
                .then()
                .log().body();
        return response;
    }

    public Response rename(String type, String name, String token) {
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .log().body()
                        .body("{\"" + type + "\": \"" + name + "\"}")
                        .when()
                        .patch(handlerUser);
        response
                .then()
                .log().body();
        return response;
    }

    public void deleteUser() {
        if (accessTokenBearer != null) {
            given()
                    .auth().oauth2(getToken())
                    .header("Content-type", "application/json")
                    .when()
                    .delete("/api/auth/user")
                    .then()
                    .log().body();
        }
    }
}

