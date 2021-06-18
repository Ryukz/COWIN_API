package cowin.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static io.restassured.RestAssured.given;

public class RunAPI {

    public Response getSlot() throws IOException, InterruptedException {
        JSONObject body=new JSONObject();
        JSONObject header=new JSONObject();
        header.put("user-agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36");
        Response response =
                given()
                        .urlEncodingEnabled(false)
                        .contentType(ContentType.JSON)
                        .accept(ContentType.ANY)
                        .with()
                        .headers(header)
                        .urlEncodingEnabled(false)
                        .when()
                        .get("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id=193&date="+printCurrentDate())
                        .then()
                        .statusCode(200)
                        .contentType(ContentType.JSON)
                        .extract()
                        .response();
        return  response;
    }

    public static String printCurrentDate(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

    public static String printCurrentDateWithTime(){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("E, dd-MMM-yyyy hh:mm a");
        String formattedDate = myDateObj.format(myFormatObj);
        return formattedDate;
    }

}
