package com.automation.core.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Map;

public class APIManager {

    public static Response sendGetRequest(String endpoint, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given().headers(headers);
        return request.get(endpoint);
    }

    public static Response sendPostRequest(String endpoint, Map<String, String> headers, String body) {
        RequestSpecification request = RestAssured.given().headers(headers).body(body);
        return request.post(endpoint);
    }

    public static Response sendPutRequest(String endpoint, Map<String, String> headers, String body) {
        RequestSpecification request = RestAssured.given().headers(headers).body(body);
        return request.put(endpoint);
    }

    public static Response sendDeleteRequest(String endpoint, Map<String, String> headers) {
        RequestSpecification request = RestAssured.given().headers(headers);
        return request.delete(endpoint);
    }
}
