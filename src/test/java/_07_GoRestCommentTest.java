
import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.baseURI;

public class _07_GoRestCommentTest{
    Faker randomUreteci=new Faker();
    int commentID=0;
    RequestSpecification reqSpec;

    @BeforeClass
    public void setup(){

        baseURI="https://gorest.co.in/public/v2/comments";

        reqSpec = new RequestSpecBuilder()
                .addHeader("Authorization","Bearer e6645dd4ebb8d733d8d88112a2d40be68efc1c0f3ee91e949f82f3630bcaad3a")
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void createComment(){
//        {
//                "post_id": 79177,
//                "name": "Omana Kaur",
//                "email": "omana_kaur@crooks.example",
//                "body": "Pariatur et consectetur."
//        }

        String fullName= randomUreteci.name().fullName();
        String email= randomUreteci.internet().emailAddress();
        String body= randomUreteci.lorem().paragraph();

        Map<String,String> newComment=new HashMap<>();
        newComment.put("post_id", "82477");
        newComment.put("name", fullName);
        newComment.put("email", email);
        newComment.put("body", body);

        commentID=
                given()
                        .spec(reqSpec)
                        .body(newComment)
                        .when()
                        .post("")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .contentType(ContentType.JSON)
                        .extract().path("id");
        ;
    }

    @Test(dependsOnMethods ="createComment" )
    public void getCommentById(){

        given()
                .spec(reqSpec)

                .when()
                .get(""+commentID)

                .then()
                .log().body()
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
        ;
    }


    @Test(dependsOnMethods ="getCommentById" )
    public void commentUpdate(){

        Map<String,String> updComment=new HashMap<>();
        updComment.put("name", "İsmet Temur");

        given()
                .spec(reqSpec)
                .body(updComment)
                .when()
                .put(""+commentID)

                .then()
                .log().body()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(commentID))
                .body("name", equalTo("İsmet Temur"))
        ;
    }

    @Test(dependsOnMethods ="commentUpdate" )
    public void deleteComment(){

        given()
                .spec(reqSpec)
                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(204)
        ;
    }

    @Test(dependsOnMethods ="deleteComment" )
    public void deleteCommentNegative(){

        given()
                .spec(reqSpec)
                .when()
                .delete(""+commentID)

                .then()
                .log().body()
                .statusCode(404)
        ;
    }

}
