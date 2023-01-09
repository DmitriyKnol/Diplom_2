package main;

import io.restassured.RestAssured;

public class BaseApi {
    public void openUri() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
}
