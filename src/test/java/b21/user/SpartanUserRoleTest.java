package b21.user;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import net.serenitybdd.junit5.SerenityTest;
import net.serenitybdd.rest.Ensure;
import net.serenitybdd.rest.SerenityRest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import spartan_util.SpartanUtil;
import static net.serenitybdd.rest.SerenityRest.reset;

@SerenityTest
public class SpartanUserRoleTest {
    @BeforeAll
    public static void init(){
        RestAssured.baseURI = "http://18.235.32.166:7000" ;
        RestAssured.basePath = "/api" ;
    }

    @ParameterizedTest
    @ValueSource(ints = {1,3,5,7,99})
    public void testUserGetSingleSpartan(int spartanIdArg){

        System.out.println("spartanIdArg = " + spartanIdArg);

        SerenityRest.given()
                .pathParam("id",spartanIdArg)
                .auth().basic("user","user").
                when()
                .get("/spartans/{id}");

        Ensure.that("Status code is 200", v-> v.statusCode(200));


    }





    @AfterAll
    public static void cleanup(){
        reset();
    }
}
