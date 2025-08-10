package com.arnabsaikia.Task08082025;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class RestfulBookerGetApis {
    RequestSpecification r;
    Response response;
    ValidatableResponse vr;
    String token;
    Integer bookingID;

    @Test(priority = 0)
    public void TC01_Get_Ping_Health_Check() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/ping");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(201);

    }

    @Test(priority = 1)
    public void TC02_Get_Booking_Via_Id() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + "962");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(200);


    }

    @Test (priority = 2)
    public void TC03_Invalid_BookingId() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + "AA");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(404);

    }

    @Test (priority = 3)
    public void TC04_Get_Booking_Ids() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(200);

    }
}
