package main;

import io.restassured.response.Response;
import main.pojo.AccessTokenBearer;
import main.pojo.CreateUser;
import main.pojo.User;

import static io.restassured.RestAssured.given;

public class UserApi {
    AccessTokenBearer accessTokenBearer;
    private static final String handlerRegister = "/api/auth/register";
    private static final String handlerLogin = "/api/auth/login";
    private static final String handlerUser = "/api/auth/user";

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

