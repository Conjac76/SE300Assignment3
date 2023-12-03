package com.se300.ledger.controller;
import com.se300.ledger.model.Transaction;


import com.se300.ledger.TestSmartStoreApplication;
import com.se300.ledger.model.Account;
import com.se300.ledger.service.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

@SpringBootTest(classes = TestSmartStoreApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LedgerRestControllerTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private TestRestTemplate restTemplate;

    private static HttpHeaders headers;

    @BeforeAll
    static void init(){

        headers = new HttpHeaders();
        headers.setBasicAuth("sergey", "chapman");
    }

    @Test
    public void testGetAccountById() throws IllegalStateException, JSONException {

        String expectedJson = "{\"address\" : \"master\", \"balance\" : 2147483647}";

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/accounts/master", HttpMethod.GET, new HttpEntity<String>(headers),
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        JSONAssert.assertEquals(expectedJson, response.getBody(),true);
    }

    @Test
    public void testPostAccount() throws IllegalStateException, JSONException {

        String expectedJson = "{\"address\" : \"sergey\", \"balance\" : 0}";
        Account account = new Account("sergey", 0);

        HttpEntity<Account> request = new HttpEntity<>(account, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:" + port + "/accounts", request, String.class);
        JSONAssert.assertEquals(expectedJson, response.getBody(),true);
    }

    @Test
    void testGetTransaction() {
        String transactionId = "transaction123";
        ResponseEntity<Transaction> response = restTemplate.exchange(
                "http://localhost:" + port + "/transactions/" + transactionId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Transaction.class);

        // Assuming your ledger returns a transaction object
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertNotNull(response.getBody()); // Ensure the response body is not null
    }

    @Test
    void testPostTransaction() {
        Transaction newTransaction = new Transaction("transaction456", 100, 20, "Test Transaction", new Account("payer", 50), new Account("receiver", 70));

        ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/transactions",
                new HttpEntity<>(newTransaction, headers),
                String.class);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertNotNull(response.getBody()); // Ensure the response body is not null
    }
}
