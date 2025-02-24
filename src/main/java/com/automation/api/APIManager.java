package com.automation.api;

import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class APIManager {

    public static Response sendGetRequest(String url) {
        return given()
                .when()
                .get(url)
                .then()
                .extract()
                .response();
    }
}
