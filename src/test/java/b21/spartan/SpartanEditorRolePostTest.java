package b21.spartan;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import spartan_util.SpartanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;


import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;

@SerenityTest
public class SpartanEditorRolePostTest {


    @BeforeAll
    public static void init(){
        baseURI = "http://18.213.0.89:7000";
        basePath = "/api";
    }

    @DisplayName("Editor Should be able Post Valid Data")
    @Test
    public void testEditorPostData(){

        Map<String,Object> bodyMap = SpartanUtil.getRandomSpartanMap();

        SerenityRest.given()
                .auth().basic("editor","editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap).
                when()
                .post("/spartans").prettyPeek();

        Ensure.that("It ran successfully ", then-> then.statusCode(201));
        Ensure.that("Response format is correct", thenSection -> thenSection.contentType(ContentType.JSON));
        Ensure.that("success message is correct ",v -> v.body("success",is("A Spartan is Born!")));
        Ensure.that("ID is generated and not null" , v-> v.body("data.id" , notNullValue()      ) ) ;
        // checking actual data
        Ensure.that("name is correct" ,
                v-> v.body("data.name" ,  is(bodyMap.get("name")) )
        ) ;
        Ensure.that("gender is correct" ,
                v-> v.body("data.gender" ,  is(bodyMap.get("gender")) )
        ) ;
        Ensure.that("phone is correct" ,
                v-> v.body("data.phone" ,  is(bodyMap.get("phone")) )
        ) ;
        // check Location header end with newly generated id
        String newId = lastResponse().path("data.id").toString() ;
        System.out.println(   lastResponse().header("Location")   );

        Ensure.that("location header end with "+ newId ,
                v-> v.header("Location" , endsWith(newId)  )
        )  ;




    }

    @AfterAll
    public static void cleanup(){
        RestAssured.reset();

    }


}
