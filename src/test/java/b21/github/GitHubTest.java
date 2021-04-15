package b21.github;

import io.restassured.RestAssured;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

//import static io.restassured.RestAssured.*;
import static net.serenitybdd.rest.SerenityRest.* ;
import static org.hamcrest.Matchers.*;

@SerenityTest
public class GitHubTest {

    // SEND GET https://api.github.com/users/CybertekSchool request

    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "https://api.github.com";
    }

    @Test
    public void testGitHubUser(){

        given().pathParam("user_id" , "CybertekSchool")
                .log().all().
                when()
                .get("/users/{user_id}").
                then()
                .log().all()
                .statusCode(200) ;

    }

    @Test
    public void testGitHubUser2(){

        SerenityRest.given()
                .pathParam("user_id" , "CybertekSchool")
                .log().all().
           when()
                .get("/users/{user_id}");

        // If you send request using SerenityRest , ->
        // the response object can be obtained from the method called lastResponse() without being saved separately
        System.out.println("SerenityRest.lastResponse().statusCode() = " + lastResponse().statusCode());
        System.out.println("lastResponse().header(\"Date\") = " + lastResponse().header("Date"));
        System.out.println("lastResponse().path(\"login\") = " + lastResponse().path("login"));
        System.out.println("lastResponse().jsonPath().getInt(\"id\") = " + lastResponse().jsonPath().getInt("id"));
        System.out.println("lastResponse().path(\"id\") = " + lastResponse().path("id"));


    }

    @DisplayName("Test GET /users/{user_id} endpoint")
    @Test
    public void testGitHubUser3() {

        SerenityRest.given()
                .pathParam("user_id", "CybertekSchool")
                .log().all().
          when()
                .get("/users/{user_id}");

        // our objective is to let each assertion show up in the report as step
        // this is the import
        Ensure.that("request ran successfully", vRes -> vRes.statusCode(200));
        Ensure.that("login field value is CybertekSchool",
                thenSection -> thenSection.body("login",is("CybertekSchool")));



    }

    @AfterAll
    public static void cleanup(){
        RestAssured.reset();
    }



}
