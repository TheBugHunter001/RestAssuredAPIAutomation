package com.arnabsaikia.Task08082025;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

public class RestfulBookerPostAndPutApis {
    RequestSpecification r;
    Response response;
    ValidatableResponse vr;
    String token;
    Integer bookingID;

    @Test(priority = 0)
    public void TC01_Create_Auth_Token() {

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


    @Test (priority = 1)
    public void TC02_Create_Booking() {

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

    @Test (priority = 2)
    public void TC03_Update_Complete_Booking() {
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

    @Test (priority = 3)
    public void TC04_Update_Partial_Booking() {
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

    @Test (priority = 4)
    public void TC05_InvalidToken_Update_Partial_Booking() {
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

    @Test (priority = 5)
    public void TC06_Unable_To_Update_due_to_BookingId_Not_Found() {
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

    @Test (priority = 6)
    public void TC07_Invalid_Auth_Token() {

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

    @Test (priority = 7)
    public void TC08_Invalid_Payload_Create_Booking() {

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
}
