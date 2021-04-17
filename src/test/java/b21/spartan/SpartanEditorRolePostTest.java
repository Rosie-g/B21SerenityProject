package b21.spartan;

import config_util.ConfigReader;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import spartan_util.SpartanUtil;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static net.serenitybdd.rest.SerenityRest.*;
import static org.hamcrest.Matchers.*;

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

    @ParameterizedTest
    @CsvSource({
            "Ercan Civi, Male, 7137324565",
            "Muhammad, Male, 9876987698",
            "Inci, Female, 7654345654"
    })
    public void testPostValidDataWithCSVSource(String name, String gender, long phone){

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name",name);
        bodyMap.put("gender", gender);
        bodyMap.put("phone", phone);
        System.out.println("bodyMap = " + bodyMap);


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

    @ParameterizedTest
    @CsvSource({
            "E Civi, Male, 7137324565, 3",
            "Muhammad, Male, 7131234565, 1",
            "Inci is from batch, female, 7654345654, 2"
    })
    public void testInvalidData(String name, String gender, long phone, int expectedErrorCount) {

        Map<String, Object> bodyMap = new LinkedHashMap<>();
        bodyMap.put("name",name);
        bodyMap.put("gender", gender);
        bodyMap.put("phone", phone);
        System.out.println("bodyMap = " + bodyMap);


        SerenityRest.given()
                .auth().basic("editor","editor")
                .log().body()
                .contentType(ContentType.JSON)
                .body(bodyMap).
                when()
                .post("/spartans").prettyPeek();

        // all iterations response status should be 400 BAD Request
        // And error count should match the expected error count
        // message field should report correct error count
        // for example if you have 3 errors it should be (Validation failed for object='spartan'. Error count: 3)

        Ensure.that("Expected 400 status code ", v -> v.statusCode(400));
        Ensure.that("Expected error count" + expectedErrorCount,
                v-> v.body("errors", hasSize(expectedErrorCount)));



    }

    @Test
    public void testingOutConfigReaderTakeItAndGoUtility(){

        System.out.println(ConfigReader.getProperty("serenity.project.name")  );
        System.out.println(ConfigReader.getProperty("spartan.rest.url") );
        // try adding more into serenity.properties and replace the hard coded values
        // like user credentials editor editor

    }



        @AfterAll
    public static void cleanup(){
        RestAssured.reset();

    }


}
