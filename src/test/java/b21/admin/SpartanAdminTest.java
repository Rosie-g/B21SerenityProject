package b21.admin;

import static io.restassured.RestAssured.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
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
        Ensure.that("Content Type is JSON ", name -> name.contentType(ContentType.JSON));

    }

    @AfterAll
    public static void cleanup(){
        RestAssured.reset();

    }


}
