package com.initializer4j.initializer.client;

import com.initializer4j.initializer.model.request.Account;
import com.initializer4j.initializer.model.response.Person;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Service
public class InitializerClient extends BasicWebClient {

  static final String PERSON_GET_ENDPOINT = "/api/person";
  static final String PERSONS_GET_ENDPOINT = "/api/persons";
  static final String ACCOUNT_LOGIN_POST_ENDPOINT = "/api/account/login";
  @Value("${services.initializer-api.base-url}")
  private String baseUrl;

  public InitializerClient(@NonNull WebClient.Builder webClientBuilder) {

    super(webClientBuilder);
  }

  public Person getPerson(String personId) {
    return genericGet(Person.class, baseUrl + PERSON_GET_ENDPOINT + "/" + personId);
  }

  public List<Person> getPersons() {
    return genericGetList(Person.class, baseUrl + PERSONS_GET_ENDPOINT);
  }

  public Person loginAccount(Account account) {
    return genericPost(Person.class, baseUrl + ACCOUNT_LOGIN_POST_ENDPOINT, account);
  }
}
