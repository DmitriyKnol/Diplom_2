package main;

import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class UserAssert {
    public void statusOk(Response status) {
        status
                .then()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    public void statusForbidden(Response status) {
        status
                .then()
                .statusCode(SC_FORBIDDEN)
                .body("success", is(false));
    }

    public void statusUnauthorized(Response status) {
        status
                .then()
                .statusCode(SC_UNAUTHORIZED)
                .body("success", is(false));
    }
}
