import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;


import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ZippoTest {

    @Test
    public void test1() {

        given()
                // Hazırlık işlemleri kodları
                .when()
                // endpoint (url), metod u verip istek gönderiliyor

                .then()
        // assertion, test, data işlemleri
        ;
    }

    @Test
    public void statusCodeTest() {

        given()
                // hazırlık kısmı boş
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json data , log().all() : gidip gelen her şey
                .statusCode(200) // test kısmı olduğundan assertion status code 200 mü
        ;
    }

    @Test
    public void contentTypeTest() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body() // dönen body json data , log().all() : gidip gelen her şey
                .statusCode(200) // test kısmı olduğundan assertion status code 200 mü
                .contentType(ContentType.JSON)  // dönen datanın tipi JSON mı
        ;
    }

    @Test
    public void test3() {

        given()

                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                //.log().body()
                .statusCode(200)  // assertion
                .body("country", equalTo("United States"));//assertion

    }

    @Test
    public void checkstateinresponsebody() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")
                .then()
                .statusCode(200)
                .body("places[0].state", equalToIgnoringCase("cAlifornia"));
    }

    @Test
    public void checkhasitem() {
        given()
                .when()
                .get("http://api.zippopotam.us/tr/59000")
                .then()
                .statusCode(200)
                .body("places.'place name'", hasItem("Kazandere Köyü"));
    }

    @Test
    public void test8() {
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .body("places.size()", equalTo(1));

    }

    @Test
    public void combiningTest() {
        given()


                .when()
                .get("http://api.zippopotam.us/us/90210")


                .then()
                .statusCode(200)
                .body("places", hasSize(1))
                .body("places[0].state", equalTo("California"))
                .body("places[0].'place name'", equalTo("Beverly Hills"));
    }

    @Test
    public void pathParamTest() {
        given()
                .pathParam("ulke", "us")
                .pathParam("postaKod", 90210)
                .log().uri()

                .when()
                .get("http://api.zippopotam.us/{ulke}/{postaKod}")

                .then()
                .statusCode(200);
    }

    @Test
    public void queryParamTest() {
        given()
                .param("page", 1)
                .log().uri()

                .when()
                .get(" https://gorest.co.in/public/v1/users")

                .then()
                .statusCode(200)
                .log().body();
    }

    @Test
    public void test11() {
        for (int i = 1; i <= 10; i++) {


            given()
                    .param("page", i)
                    .log().uri()

                    .when()
                    .get(" https://gorest.co.in/public/v1/users")

                    .then()
                    .statusCode(200)
                    //.log().body()
                    .body("meta.pagination.page", equalTo(i));
        }
    }

    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    @BeforeClass
    public void setup() {
        baseURI = "https://gorest.co.in/public/v1";

        requestSpec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .log(LogDetail.URI)
                .build();
        responseSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .log(LogDetail.BODY)
                .expectContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void requestResponseSpecificationn() {
        given()
                .param("page", 1)
                .spec(requestSpec)

                .when()
                .get("/users")

                .then()
                .spec(responseSpec);
    }

    @Test
    public void extractingJsonPath() {
        String countryname =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().path("country");
        System.out.println("country = " + countryname);
        Assert.assertEquals(countryname, "California");
    }

    @Test
    public void extractingJsonPath2() {
        String statename =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().path("places[0].state");
        System.out.println("statename = " + statename);
        Assert.assertEquals(statename, "California");
    }

    @Test
    public void extractingJsonPath3() {
        String placename =
                given()
                        .when()
                        .get("http://api.zippopotam.us/us/90210")
                        .then()
                        .extract().path("places[0].'place name'");
        System.out.println("placename = " + placename);
        Assert.assertEquals(placename, "Beverly Hills");
    }

    @Test
    public void extractingJsonPath4() {
        int limit=
        given()
                .when()
                .get("https://gorest.co.in/public/v1/users")
                .then()
                .extract().path("meta.pagination.limit");
        System.out.println("limit = " + limit);
        Assert.assertTrue(limit== 10);

    }

    @Test
    public void extractingJsonPath5(){
        List<Integer> idler=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.id");
        ;

        System.out.println("idler = " + idler);
    }

    @Test
    public void extractingJsonPath6(){
        List<String>names=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().path("data.name");

        System.out.println("names = " + names);
    }

    @Test
    public void extractinJsonPathResponseAll(){
        Response donenData=
                given()
                        .when()
                        .get("https://gorest.co.in/public/v1/users")

                        .then()
                        .extract().response();

        List<Integer> idler =donenData.path("data.id");
        List<String> isimler=donenData.path("data.name");
        int limit=donenData.path("meta.pagination.limit");

        System.out.println("idler = " + idler);
        System.out.println("isimler = " + isimler);
        System.out.println("limit = " + limit);

        Assert.assertTrue(isimler.contains("Mahesh Menon"));
        Assert.assertTrue(idler.contains(5599126));
        Assert.assertTrue(limit==10);
    }

}
