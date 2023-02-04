package main;

import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.is;

public class OrderAssert {
    public void statusOk(Response status) {
        status
                .then()
                .statusCode(SC_OK)
                .body("success", is(true));
    }

    public void statusBadRequest(Response status) {
        status
                .then()
                .statusCode(SC_BAD_REQUEST)
                .body("success", is(false));
    }

    public void statusInternalServerError(Response status) {
        status
                .then()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }
}
