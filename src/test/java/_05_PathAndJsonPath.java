import Model.Location;
import Model.Place;
import io.restassured.response.Response;
import org.testng.annotations.*;

import java.util.List;

import static io.restassured.RestAssured.*;

public class _05_PathAndJsonPath {
    @Test
    public void extractingPath(){
        String postCode=  // int e dönüşüm istediğimizde hata aldık.
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().path("'post code'")
                ;

        System.out.println("postCode = " + postCode);
    }

    @Test
    public void extractingJosnPath()
    {
        int postCode=  // int e dönüşüm de JsonPath yönteminde hata almadık
                given()

                        .when()
                        .get("http://api.zippopotam.us/us/90210")

                        .then()
                        //.log().body()
                        .extract().jsonPath().getInt("'post code'")
                // tip dönüşümü otomatik, uygun tip verilmeli
                ;

        System.out.println("postCode = " + postCode);
    }
    @Test
    public void getZipCode(){
        Response response=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .log().body()
                .extract().response();

        Location locationPathAs =response.as(Location.class);
        System.out.println("locationPathAs.getPlaces() = " + locationPathAs.getPlaces());

        List<Place> places=response.jsonPath().getList("places",Place.class);
        System.out.println("places = " + places);
        // Daha önceki örneklerde (as) Clas dönüşümleri için tüm yapıya karşılık gelen
        // gereken tüm classları yazarak dönüştürüp istediğimiz elemanlara ulaşıyorduk.

        // Burada ise(JsonPath) aradaki bir veriyi clasa dönüştürerek bir list olarak almamıza
        // imkan veren JSONPATH i kullandık.Böylece tek class ile veri alınmış oldu
        // diğer class lara gerek kalmadan

        // path : class veya tip dönüşümüne imkan veremeyen direk veriyi verir. List<String> gibi
        // jsonPath : class dönüşümüne ve tip dönüşümüne izin vererek , veriyi istediğimiz formatta verir.
    }
}

