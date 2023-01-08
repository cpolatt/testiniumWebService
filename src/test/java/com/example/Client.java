package com.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class Client {

    public static final String apiKey = "49f20e3cc8c1575a91aca79dcd7939fe";
    public static final String token = "ATTA42afe80c58654adbe9477950c816f111f4cb737bf77faa7a615cd9440adb04b3A66ADCB1";

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://api.trello.com/1";
    }

    @Test
    public void TrelloRequest() {

        // Create Board
        String BoardRequestBody = "{\"name\": \"My New Board\", \"defaultLists\": false}";
        Response BoardResponse = given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .body(BoardRequestBody)
                .when()
                .post("/boards")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        String boardId = BoardResponse.jsonPath().getString("id");

        // Create List
        String ListrequestBody = "{\"name\": \"My New List\", \"idBoard\": \"" + boardId + "\"}";
        Response ListResponse = given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .body(ListrequestBody)
                .when()
                .post("/lists")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        String listId = ListResponse.jsonPath().getString("id");

        // Create List
        String CardRequestBody = "{\"name\": \"My New Card\", \"idList\": \"" + listId + "\"}";
        String CardRequestBody2 = "{\"name\": \"My New Card2\", \"idList\": \"" + listId + "\"}";

        Response CardResponse = given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .body(CardRequestBody)
                .when()
                .post("/cards")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        Response CardResponse2 = given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .body(CardRequestBody2)
                .when()
                .post("/cards")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        String cardId = CardResponse.jsonPath().getString("id");
        String cardId2 = CardResponse2.jsonPath().getString("id");

        // Update card
        String newName = "Updated Card Name";
        String newDescription = "Updated card description.";

        given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .contentType("application/json")
                .body(String.format("{\"name\":\"%s\",\"desc\":\"%s\"}", newName, newDescription))
                .when()
                .put(String.format("/cards/%s", cardId))
                .then()
                .statusCode(200);

        // Delete card
        given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .when()
                .delete(String.format("/cards/%s", cardId))
                .then()
                .statusCode(200);
        given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .when()
                .delete(String.format("/cards/%s", cardId2))
                .then()
                .statusCode(200);

        // Delete Board
        given()
                .queryParam("key", apiKey)
                .queryParam("token", token)
                .when()
                .delete(String.format("/boards/%s", boardId))
                .then()
                .statusCode(200);

    }
}