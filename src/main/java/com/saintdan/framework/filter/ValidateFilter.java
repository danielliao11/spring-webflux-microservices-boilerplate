package com.saintdan.framework.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintdan.framework.annotation.NotNullField;
import com.saintdan.framework.annotation.SizeField;
import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.enums.ResourceUri;
import com.saintdan.framework.vo.ErrorVO;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Validate param field.
 * // TODO Multiple body reader for ServerHttpRequest.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Component
public class ValidateFilter implements WebFilter {

  @Override
  @SuppressWarnings("unchecked")
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerWebExchange tmpExchange = exchange.mutate().request(request).build();
    ObjectMapper mapper = new ObjectMapper();
    if (request.getMethod() == null) {
      exchange.getResponse().setStatusCode(HttpStatus.NOT_FOUND);
      return Mono.empty();
    }
    return matches(request.getMethod().name())
        .flatMap(b -> request.getBody().next())
        .flatMap(dataBuffer -> {
          try {
            return Mono.just(mapper.readValue(dataBuffer.asInputStream(),
                ResourceUri.resolve(request.getURI().getPath()).clazz()));
          } catch (IOException e) {
            return Mono.just(e);
          }
        })
        .flatMap(o -> {
          ErrorVO vo = validate(o, request.getMethod());
          if (vo != null) {
              try {
                exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
                exchange.getResponse().getHeaders().put(HttpHeaders.CONTENT_TYPE,
                    Collections.singletonList(MediaType.APPLICATION_JSON_UTF8_VALUE));
                return exchange.getResponse()
                    .writeWith(Flux.just(exchange.getResponse().bufferFactory()
                        .wrap(mapper.writeValueAsBytes(vo))));
              } catch (JsonProcessingException e) {
                Mono.just(e);
              }
            }
            return Mono.empty();
        })
        .switchIfEmpty(chain.filter(tmpExchange));
  }

  /**
   * Bean properties null validation.
   *
   * @param param  Param bean
   * @param method {@link HttpMethod}
   * @return error
   */
  private ErrorVO validate(Object param, HttpMethod method) {
    Field[] fields = param.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field == null || !field.isAnnotationPresent(NotNullField.class)) {
        continue; // Ignore field without ParamField annotation.
      }
      field.setAccessible(true);
      NotNullField notNullField = field.getAnnotation(NotNullField.class);
      try {
        if (ArrayUtils.contains(notNullField.value(), method) && ObjectUtils
            .isEmpty(field.get(param))) {
          return ErrorVO.builder().error(ErrorType.SYS0002.name())
              .error_description(notNullField.message()).build();
        }
      } catch (IllegalAccessException ignore) {
      }
      if (field.isAnnotationPresent(SizeField.class)) {
        SizeField size = field.getAnnotation(SizeField.class);
        try {
          if (ArrayUtils.contains(size.value(), method)
              && (field.get(param).toString().length() > size.max()
              || field.get(param).toString().length() < size.min())) {
            return ErrorVO.builder().error(ErrorType.SYS0002.name())
                .error_description(notNullField.message()).build();
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
    return null;
  }

  private static final Map<String, String> mappings = new HashMap<>(3);

  static {
    mappings.put(HttpMethod.POST.name(), HttpMethod.POST.name());
    mappings.put(HttpMethod.PUT.name(), HttpMethod.PUT.name());
    mappings.put(HttpMethod.PATCH.name(), HttpMethod.PATCH.name());
  }

  private Mono<Boolean> matches(String method) {
    return Mono.just(mappings.containsKey(method));
  }
}
