package com.arnabsaikia.Task05082025;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class RestfulBookerApiWithTestNG {

    Integer bookingId;

    // Create booking with POST API
    @Test
    public void TC1_CreateBooking() {

        String payload = "{\n" +
                "    \"firstname\" : \"Jim\",\n" +
                "    \"lastname\" : \"Brown\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking")
                .header("Content-Type", "application/json")
                .when()
                .body(payload)
                .post();

        response.then().log().all().statusCode(200);

        this.bookingId = response.jsonPath().getInt("bookingid"); // <<<< Extract booking id

    }

    // Fetch booking with GET API
    @Test
    public void TC2_GetBookingId() {

        RestAssured.given()
                .baseUri("https://restful-booker.herokuapp.com")
                .basePath("/booking/" + bookingId)
                .when()
                .get()
                .then().log().all().statusCode(200);

    }
}
