package com.se300.ledger.controller;

import com.se300.ledger.TestSmartStoreApplication;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.TimeToLive;
import org.mockserver.matchers.Times;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

@SpringBootTest(classes = TestSmartStoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LedgerRestInternalMockControllerTest {

    @LocalServerPort
    private static Integer port;

    @BeforeAll
    static void init(){

        ClientAndServer.startClientAndServer(1090);

        new MockServerClient("localhost", 1090)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/accounts"),
                        Times.unlimited(),
                        TimeToLive.unlimited(),
                        0
                )
                .respond(
                        response()
                                .withBody("{\n  \"address\" : \"master\",\n \"balance\" : 2147483647\n}")
                );
    }
    @Test
    void testGetAccountById() throws JSONException {

        String expectedJson = "{\"address\" : \"master\", \"balance\" : 2147483647}";

        ExtractableResponse<Response> response = RestAssured
                .given()
                .filter(new RequestLoggingFilter())
                .auth().basic("sergey", "chapman")
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:" + 1090 + "/accounts")
                .then()
                .statusCode(200)
                .extract();

        JSONAssert.assertEquals(expectedJson, response.body().asPrettyString(),true);
    }

    @Test
    public void testGetTransactionById() throws JSONException {

        String expectedTransactionJson = "{\"transactionId\":\"1\", \"amount\":100, \"fee\":10, \"note\":\"Test Transaction\", \"payer\":{\"address\":\"payerAddress\", \"balance\":100}, \"receiver\":{\"address\":\"receiverAddress\", \"balance\":50}}";

        // Setup the MockServerClient to handle the expected transaction request
        new MockServerClient("localhost", 1090)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/transactions/1"),
                        Times.unlimited(),
                        TimeToLive.unlimited(),
                        0
                )
                .respond(
                        response()
                                .withBody(expectedTransactionJson)
                );

        // Perform the request to retrieve the transaction
        ExtractableResponse<Response> transactionResponse = RestAssured
                .given()
                .filter(new RequestLoggingFilter())
                .auth().basic("sergey", "chapman")
                .contentType(ContentType.JSON)
                .when()
                .get("http://localhost:" + 1090 + "/transactions/1")
                .then()
                .statusCode(200)
                .extract();

        // Verify that the returned transaction matches the expected JSON
        JSONAssert.assertEquals(expectedTransactionJson, transactionResponse.body().asPrettyString(),true);
    }
}