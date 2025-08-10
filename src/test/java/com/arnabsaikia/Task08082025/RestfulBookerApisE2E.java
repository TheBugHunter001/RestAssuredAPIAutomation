package com.arnabsaikia.Task08082025;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class RestfulBookerApisE2E {
    RequestSpecification r;
    Response response;
    ValidatableResponse vr;
    String token;
    Integer bookingID;


    @Test (priority = 0)
    public void TC01_Get_Ping_Health_Check() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/ping");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(201);

    }

    @Test (priority = 1)
    public void TC02_Create_Auth_Token() {

        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password123\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/auth");
        r.contentType(ContentType.JSON);
        r.body(payload).log().all();

        response = r.when().log().all().post();

        vr = response.then().log().all().statusCode(200);

        this.token = response.jsonPath().getString("token");

    }


    @Test (priority = 2)
    public void TC03_Create_Booking() {

        String payload = "{\n" +
                "    \"firstname\" : \"Bruce\",\n" +
                "    \"lastname\" : \"Wayne\",\n" +
                "    \"totalprice\" : 111,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking");
        r.contentType(ContentType.JSON);
        r.body(payload).log().all();

        response = r.when().log().all().post();

        vr = response.then().log().all().statusCode(200);

        this.bookingID = response.jsonPath().getInt("bookingid");

    }

    @Test (priority = 3)
    public void TC04_Update_Complete_Booking() {
        String payload = "{\n" +
                "    \"firstname\" : \"Clark\",\n" +
                "    \"lastname\" : \"Kent\",\n" +
                "    \"totalprice\" : 222,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + bookingID);
        r.contentType(ContentType.JSON);
        r.cookie("token", token);
        r.body(payload).log().all();

        response = r.when().log().all().put();

        vr = response.then().log().all().statusCode(200);

    }

    @Test (priority = 4)
    public void TC05_Update_Partial_Booking() {
        String payload = "{\n" +
                "    \"firstname\" : \"Tony\",\n" +
                "    \"lastname\" : \"Stark\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + bookingID);
        r.contentType(ContentType.JSON);
        r.cookie("token", token);
        r.body(payload).log().all();

        response = r.when().log().all().patch();

        vr = response.then().log().all().statusCode(200);

    }

    @Test(priority = 5)
    public void TC06_Get_Booking_Via_Id() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + bookingID);
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(200);


    }

    @Test (priority = 6)
    public void TC07_Invalid_BookingId() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + "AA");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(404);

    }

    @Test (priority = 7)
    public void TC08_Get_Booking_Ids() {

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking");
        response = r.when().log().all().get();
        vr = response.then().log().all().statusCode(200);

    }

    @Test (priority = 8)
    public void TC09_InvalidToken_Update_Partial_Booking() {
        String payload = "{\n" +
                "    \"firstname\" : \"Tony\",\n" +
                "    \"lastname\" : \"Stark\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + bookingID);
        r.contentType(ContentType.JSON);
        r.cookie("token", "asdasds");
        r.body(payload).log().all();

        response = r.when().log().all().patch();

        vr = response.then().log().all().statusCode(403);

    }

    @Test (priority = 9)
    public void TC10_Unable_To_Update_due_to_BookingId_Not_Found() {
        String payload = "{\n" +
                "    \"firstname\" : \"Clark\",\n" +
                "    \"lastname\" : \"Kent\",\n" +
                "    \"totalprice\" : 222,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2018-01-01\",\n" +
                "        \"checkout\" : \"2019-01-01\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + "@#$");
        r.contentType(ContentType.JSON);
        r.cookie("token", token);
        r.body(payload).log().all();

        response = r.when().log().all().put();

        vr = response.then().log().all().statusCode(405);

    }

    @Test (priority = 10)
    public void TC11_Invalid_Auth_Token() {

        String payload = "{\n" +
                "    \"username\" : \"admin\",\n" +
                "    \"password\" : \"password1234567\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/auth");
        r.contentType(ContentType.JSON);
        r.body(payload).log().all();

        response = r.when().log().all().post();

        vr = response.then().log().all().statusCode(200);

    }

    @Test (priority = 11)
    public void TC12_Invalid_Payload_Create_Booking() {

        String payload = "{\n" +
                "    \"firstname\" : \"Tony\",\n" +
                "    \"lastname\" : \"Stark\"\n" +
                "}";

        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking");
        r.contentType(ContentType.JSON);
        r.body(payload).log().all();

        response = r.when().log().all().post();

        vr = response.then().log().all().statusCode(500);

    }

    @Test (priority = 12)
    public void TC13_DeleteBooking() {
        r = RestAssured.given();
        r.baseUri("https://restful-booker.herokuapp.com");
        r.basePath("/booking/" + bookingID);
        r.contentType(ContentType.JSON);
        // r.header("Cookie","token="+token);
        r.cookie("token", token);


        response = r.when().log().all().delete();

        vr = response.then().log().all();
        vr.statusCode(201);


    }
}
