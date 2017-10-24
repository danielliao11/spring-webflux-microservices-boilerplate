package com.saintdan.framework.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.saintdan.framework.annotation.NotNullField;
import com.saintdan.framework.annotation.SizeField;
import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.param.UserParam;
import com.saintdan.framework.vo.ErrorVO;
import java.io.IOException;
import java.lang.reflect.Field;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Validate param field.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Component
public class ValidateFilter implements WebFilter {

  @Override public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    ServerWebExchange tmpExchange = exchange.mutate().request(request).build();
    ObjectMapper mapper = new ObjectMapper();
    return request.getBody()
        .filter(a -> request.getMethod() != null
            && (request.getMethod().matches(HttpMethod.POST.name())
            || request.getMethod().matches(HttpMethod.PUT.name())
            || request.getMethod().matches(HttpMethod.PATCH.name())))
        .next()
        .flatMap(dataBuffer -> {
          try {
            return Mono.just(mapper.readValue(dataBuffer.asInputStream(), UserParam.class));
          } catch (IOException e) {
            return Mono.just(new RuntimeException(e));
          }
        })
        .flatMap(o -> {
          ErrorVO vo = validate(o, request.getMethod());
          if (vo == null) {
            return chain.filter(tmpExchange);
          }
          exchange.getResponse().setStatusCode(HttpStatus.UNPROCESSABLE_ENTITY);
          return Mono.empty();
        });
  }

  /**
   * Bean properties null validation.
   *
   * @param param  Param bean
   * @param method {@link HttpMethod}
   * @return error
   */
  public ErrorVO validate(Object param, HttpMethod method) {
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
}
