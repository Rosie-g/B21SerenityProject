package b21.library;

import config_util.ConfigReader;
import net.serenitybdd.junit5.SerenityTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import config_util.ConfigReader;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import static net.serenitybdd.rest.SerenityRest.*;
import static io.restassured.RestAssured.basePath;
import static io.restassured.RestAssured.baseURI;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.is;

@SerenityTest
public class LibraryTest {

    @BeforeAll
    public static void init(){
        baseURI  = ConfigReader.getProperty("base.url");
        basePath = ConfigReader.getProperty("base.path") ;
    }


    @Test
    public void testReadingConfProperties(){

        System.out.println( ConfigReader.getProperty("base.url")  );

    }

    @DisplayName("Login and Get Dashboard info")
    @Test
    public void testDashboardStats(){

        String token = given()
                .contentType(ContentType.URLENC)
                .formParam("email",ConfigReader.getProperty("librarian.username"))
                .formParam("password",ConfigReader.getProperty("librarian.password")).
                when()
                .post("/login").prettyPeek()
                .path("token");


        SerenityRest.
                given()
                .header("X-LIBRARIAN-TOKEN",token).
                when()
                .get("/dashboard-stats").prettyPeek();

    }


    @AfterAll
    public static void cleanup(){

        reset();

    }



}