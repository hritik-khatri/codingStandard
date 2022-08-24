package com.initializer4j.initializer.client;

import com.initializer4j.initializer.exception.Initializer4jException;
import com.initializer4j.initializer.model.request.Account;
import com.initializer4j.initializer.model.response.Person;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class InitializerClientTest {

  InitializerClient initializerClient;
  private MockWebServer server;
  private String host;

  @Test
  void whenGetPerson_getSuccessfulResponse() throws IOException, InterruptedException {

    server = new MockWebServer();
    server.start();
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .setBody("{}"));
    host = server.url("/api/person/").toString();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);
    Person result = initializerClient.getPerson("/CODE1");

    RecordedRequest request = server.takeRequest();
    assertEquals("GET", request.getMethod());
    assertThat(request.getPath()).startsWith("/api/person/");
    assertThat(request.getPath()).endsWith("CODE1");

    assertNotNull(result);
    server.shutdown();
  }

  @Test
  void whenGetPerson_get400Error() throws IOException {

    server = new MockWebServer();
    server.start();
    server.enqueue(new MockResponse().setResponseCode(400).setBody("400 Error"));
    host = server.url("/api/person/").toString();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);

    Exception thrown =
        assertThrows(Initializer4jException.class, () -> initializerClient.getPerson("CODE"));

    assertEquals("400 Error", thrown.getMessage());
    server.shutdown();
  }

  @Test
  void whenGetPersons_getSuccessfulResponse() throws IOException, InterruptedException {

    server = new MockWebServer();
    server.start();
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .setBody("[{}]"));
    host = server.url("/api/persons").toString();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);
    List<Person> result = initializerClient.getPersons();

    RecordedRequest request = server.takeRequest();
    assertEquals("GET", request.getMethod());
    assertThat(request.getPath()).startsWith("/api/persons");

    assertNotNull(result);
    server.shutdown();
  }

  @Test
  void whenGetPersons_get400Error() throws IOException {

    server = new MockWebServer();
    server.start();
    server.enqueue(new MockResponse().setResponseCode(400).setBody("400 Error"));
    host = server.url("/api/persons").toString();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);

    Exception thrown =
        assertThrows(Initializer4jException.class, () -> initializerClient.getPersons());

    assertEquals("400 Error", thrown.getMessage());
    server.shutdown();
  }

  @Test
  void whenLoginAccount_getSuccessfulResponse() throws IOException, InterruptedException {
    server = new MockWebServer();
    server.start();
    server.enqueue(
        new MockResponse()
            .setResponseCode(200)
            .addHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
            .setBody("{}"));
    host = server.url("/api/account/login").toString();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);
    Person result = initializerClient.loginAccount(new Account());

    RecordedRequest request = server.takeRequest();
    assertEquals("POST", request.getMethod());
    assertThat(request.getPath()).startsWith("/api/account/login");
    assertThat(request.getPath()).endsWith("login");

    assertNotNull(result);
    server.shutdown();
  }

  @Test
  void whenLoginAccount_get400Error() throws IOException {
    server = new MockWebServer();
    server.start();
    server.enqueue(new MockResponse().setResponseCode(400).setBody("400 Error"));
    host = server.url("/api/account/login").toString();
    Account account = new Account();

    initializerClient = new InitializerClient(WebClient.builder());
    setField(initializerClient, "baseUrl", host);
    Exception thrown =
        assertThrows(Initializer4jException.class, () -> initializerClient.loginAccount(account));

    assertEquals("400 Error", thrown.getMessage());
    server.shutdown();
  }
}
