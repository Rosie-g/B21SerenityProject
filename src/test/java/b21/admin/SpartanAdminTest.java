package b21.admin;

import static io.restassured.RestAssured.*;

import io.cucumber.java.af.En;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class SpartanAdminTest {

    @BeforeAll
    public static void init(){
        baseURI = "http://18.213.0.89:7000";
        basePath = "/api";
    }

    @DisplayName("Test Admin /spartans endpoint")
    @Test
    public void testAllSpartans(){

        SerenityRest.given()
                .auth().basic("admin","admin")
                .log().all().
                when()
                .get("/spartans");



        Ensure.that("Successful 200 Response ", p -> p.statusCode(200));
        Ensure.that("Response format as expected ", name -> name.contentType(ContentType.JSON));
        // check json array size
        Ensure.that("Response has correct size ", vRes->vRes.body("",hasSize(102)));


    }

    @DisplayName("Test public user GET /spartans endpoint")
    @Test
    public void testPublicUserSpartanData(){

        SerenityRest.when().get("/spartans");
        Ensure.that("Public user should not be able to get all spartans",p-> p.statusCode(401));

    }



    @AfterAll
    public static void cleanup(){
        RestAssured.reset();

    }


}
