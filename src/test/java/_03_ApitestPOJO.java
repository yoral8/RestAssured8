import Model.Location;
import Model.Place;
import io.restassured.RestAssured;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class _03_ApitestPOJO {
    // POJO : JSON nesnesi : LocationNesnesi

    @Test
    public void extractJSONAllPOJO(){
        Location locationNesnesi=
        given()
                .when()
                .get("http://api.zippopotam.us/us/90210")

                .then()
                .extract().body().as(Location.class);
        System.out.println("locationNesnesi.getCountry() = " + locationNesnesi.getCountry());
        System.out.println("locationNesnesi.getPlaces() = " + locationNesnesi.getPlaces());

        for (Place p:locationNesnesi.getPlaces()) {
            System.out.println("p = " + p);

        }
    }
    @Test
    public void extractPOJO_soru(){
        Location locationnesnes=
                given()
                        .when()
                        .get("http://api.zippopotam.us/tr/59000")

                        .then()
                        .extract().body().as(Location.class);
        for (Place p: locationnesnes.getPlaces())
        {
            if (p.getPlacename().equalsIgnoreCase("Kazandere Köyü")){
                System.out.println("p = " + p);
            }
        }
    }









}
