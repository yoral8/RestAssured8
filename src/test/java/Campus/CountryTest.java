package Campus;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;

public class CountryTest {

    Faker randomUreteci=new Faker();
    RequestSpecification reqSpec;
    String countryID="";
    String rndCountryName="";
    String rndCountryCode="";

    @BeforeClass
    public void Setup(){
        baseURI="https://test.mersys.io/";

        Map<String,String> userCredential=new HashMap<>();
        userCredential.put("username","turkeyts");
        userCredential.put("password","TechnoStudy123");
        userCredential.put("rememberMe","true");

        Cookies cookies=
                given()
                        .body(userCredential)
                        .contentType(ContentType.JSON)
                        .when()
                        .post("/auth/login")

                        .then()
                        //.log().all()
                        .statusCode(200)
                        .extract().response().getDetailedCookies();

        reqSpec=new RequestSpecBuilder()
                .addCookies(cookies)
                .setContentType(ContentType.JSON)
                .build();
    }
    @Test
    public void createCountry(){
        rndCountryName=randomUreteci.address().country()+randomUreteci.address().countryCode();
        rndCountryCode=randomUreteci.address().countryCode();

        Map<String,String> newCountry=new HashMap<>();
        newCountry.put("name",rndCountryName);
        newCountry.put("code",rndCountryCode);

        countryID=
        given()
                .spec(reqSpec)
                .body(newCountry)
                //.log().all()

                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id")
        ;


    }
    @Test(dependsOnMethods = "createCountry")
    public void negativeCountryTest(){
        Map<String,String> newCountry=new HashMap<>();
        newCountry.put("name",rndCountryName);
        newCountry.put("code",rndCountryCode);

                given()
                        .spec(reqSpec)
                        .body(newCountry)
                        //.log().all()

                        .when()
                        .post("school-service/api/countries")

                        .then()
                        .log().body()
                        .statusCode(400)
                        .body("message",containsString("already"))
        ;
    }
    @Test(dependsOnMethods ="negativeCountryTest")
    public void updateCountry(){
        String newCountryName="Update Country"+randomUreteci.number().digits(5);
        Map<String,String> updCountry=new HashMap<>();
        updCountry.put("id",countryID);
        updCountry.put("name",newCountryName);
        updCountry.put("code","12345");

        given()
                .spec(reqSpec)
                .body(updCountry)
                .when()
                .put("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(200)
                .body("name", equalTo(newCountryName));

    }
    @Test(dependsOnMethods = "updateCountry")
    public void deleteCountry(){
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(200);
    }
    @Test(dependsOnMethods = "deleteCountry")
    public void negativeDeleteCountry(){
        given()
                .spec(reqSpec)
                .when()
                .delete("school-service/api/countries/"+countryID)

                .then()
                .log().body()
                .statusCode(400)
                .body("message",equalTo("Country not found"));
    }
    @Test
    public void createCountryAllParamater(){

        rndCountryName= randomUreteci.address().country()+randomUreteci.address().countryCode();
        rndCountryCode= randomUreteci.address().countryCode();

        Object[] arr=new Object[1];
        Map<String,Object> newCountry=new HashMap<>();
        newCountry.put("name",rndCountryName);
        newCountry.put("code",rndCountryCode);
        newCountry.put("translateName", new Object[1] ); //arr

        given()
                .spec(reqSpec)
                .body(newCountry)
                //.log().all()
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");
        ;
    }

    @Test
    public void createCountryAllParamaterClass(){

        rndCountryName= randomUreteci.address().country()+randomUreteci.address().countryCode();
        rndCountryCode= randomUreteci.address().countryCode();

        Country newCountry=new Country();
        newCountry.name=rndCountryName;
        newCountry.code=rndCountryCode;
        newCountry.translateName = new Object[1];

        given()
                .spec(reqSpec)
                .body(newCountry)
                //.log().all()
                .when()
                .post("school-service/api/countries")

                .then()
                .log().body()
                .statusCode(201)
                .extract().path("id");
        ;
    }
}
