package main;

import io.restassured.response.Response;
import main.pojo.Ingredients;

import java.util.List;

import static io.restassured.RestAssured.given;

public class OrderApi {
    private static final String handlerGetOrder = "/api/orders";
    String ingredient1;
    String ingredient2;
    Ingredients invalIdIngredients = new Ingredients(List.of("nononohash", "nohashtoo"));
    Ingredients nullIngredients = new Ingredients();
    Ingredients ingredientsValid;

    public Ingredients getInvalIdIngredients() {
        return invalIdIngredients;
    }

    public Ingredients getIngredientsValid() {
        return ingredientsValid;
    }

    public Ingredients getNullIngredients() {
        return nullIngredients;
    }

    public Response getOrder(String token) {
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .when()
                        .get(handlerGetOrder);
        return response;
    }

    public void getIngredients() {
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

    public Response createOrder(Ingredients ing, String token) {
        Response response =
                given()
                        .auth().oauth2(token)
                        .header("Content-type", "application/json")
                        .log().body()
                        .body(ing)
                        .when()
                        .post(handlerGetOrder);
        return response;
    }


}
