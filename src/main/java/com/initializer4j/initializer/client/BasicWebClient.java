package com.initializer4j.initializer.client;

import com.initializer4j.initializer.exception.ErrorCode;
import com.initializer4j.initializer.exception.Initializer4jException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public abstract class BasicWebClient {

  protected final WebClient webClient;

  protected BasicWebClient(@NonNull WebClient.Builder webClientBuilder) {

    this.webClient =
        webClientBuilder
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString())
            .build();
  }

  private WebClient.RequestBodySpec buildRequestBodySpec(
      HttpMethod httpMethod, String path, Object... params) {
    WebClient.RequestBodyUriSpec uriSpec = webClient.method(httpMethod);
    if (params == null || params.length == 0) {
      return uriSpec.uri(path).header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    } else {
      return uriSpec.uri(path, params).header(CONTENT_TYPE, APPLICATION_JSON_VALUE);
    }
  }

  protected <T, S> T genericPost(Class<T> tClass, String path, S body) {
    return genericPost(tClass, path, body, (Object) null);
  }

  protected <T, S> T genericPost(Class<T> tClass, String path, S body, Object... params) {
    var uriSpec = buildRequestBodySpec(HttpMethod.POST, path, params);
    var spec = uriSpec.body(BodyInserters.fromValue(body));
    log.debug(String.format("calling POST on initializer api %s", path));
    return retrieveToFluxWithRetry(spec, tClass);
  }

  protected <T> T genericGet(Class<T> tClass, String path) {
    return genericGet(tClass, path, (Object) null);
  }

  protected <T> T genericGet(Class<T> tClass, String path, Object... params) {
    log.debug(String.format("calling GET on initializer api %s", path));
    var uriSpec = buildRequestBodySpec(HttpMethod.GET, path, params);
    return retrieveToFluxWithRetry(uriSpec, tClass);
  }

  protected <T> List<T> genericGetList(Class<T> tClass, String path) {
    return genericGetList(tClass, path, (Object) null);
  }

  protected <T> List<T> genericGetList(Class<T> tClass, String path, Object... params) {
    log.debug(String.format("calling GET on initializer api %s", path));
    var uriSpec = buildRequestBodySpec(HttpMethod.GET, path, params);
    return retrieveToFluxWithRetryList(uriSpec, tClass);
  }

  private <T> List<T> retrieveToFluxWithRetryList(
      WebClient.RequestHeadersSpec<?> requestHeadersSpec, Class<T> tClass) {
    return requestHeadersSpec
        .retrieve()
        .onStatus(HttpStatus::isError, this::handleError)
        .bodyToMono(new ParameterizedTypeReference<List<T>>() {})
        .block();
  }

  private <T> T retrieveToFluxWithRetry(
      WebClient.RequestHeadersSpec<?> requestHeadersSpec, Class<T> tClass) {
    return requestHeadersSpec
        .retrieve()
        .onStatus(HttpStatus::isError, this::handleError)
        .bodyToMono(tClass)
        .block();
  }

  public Mono<Initializer4jException> handleError(ClientResponse response) {
    log.error("Failed downstream call to Initializer - response : {}", response.rawStatusCode());

    return response
        .bodyToMono(String.class)
        .flatMap(
            errorBody ->
                Mono.error(new Initializer4jException(errorBody, ErrorCode.SERVICE_UNAVAILABLE)));
  }
}
