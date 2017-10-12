package com.saintdan.framework.component;

import com.saintdan.framework.annotation.NotNullField;
import com.saintdan.framework.annotation.SizeField;
import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.enums.GrantType;
import com.saintdan.framework.enums.OperationType;
import java.lang.reflect.Field;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Component
public class ValidateHelper {

  /**
   * Bean properties null validation.
   *
   * @param param Param bean
   * @param operationType {@link OperationType}
   * @return true / false
   */
  public Mono<ServerResponse> validate(Object param, OperationType operationType) {
    Field[] fields = param.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field == null || !field.isAnnotationPresent(NotNullField.class)) {
        continue; // Ignore field without ParamField annotation.
      }
      field.setAccessible(true);
      NotNullField notNullField = field.getAnnotation(NotNullField.class);
      try {
        if (ArrayUtils.contains(notNullField.value(), operationType) && (field.get(param) == null
            || StringUtils.isBlank(field.get(param).toString()))) {
          return resultHelper
              .infoResp(ErrorType.SYS0002, notNullField.message(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
      } catch (IllegalAccessException ignore) {
      }
      if (field.isAnnotationPresent(SizeField.class)) {
        SizeField size = field.getAnnotation(SizeField.class);
        try {
          if (ArrayUtils.contains(size.value(), operationType)
              && (field.get(param).toString().length() > size.max()
              || field.get(param).toString().length() < size.min())) {
            return resultHelper
                .infoResp(ErrorType.SYS0002, size.message(), HttpStatus.UNPROCESSABLE_ENTITY);
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
    return Mono.empty();
  }

  /**
   * Bean properties null validation for auth.
   *
   * @param param Param bean
   * @param grantType {@link GrantType}
   * @return 422
   */
  public Mono<ServerResponse> validate(Object param, GrantType grantType) {
    Field[] fields = param.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field == null || !field.isAnnotationPresent(NotNullField.class)) {
        continue; // Ignore field without ParamField annotation.
      }
      field.setAccessible(true);
      NotNullField notNullField = field.getAnnotation(NotNullField.class);
      try {
        if (ArrayUtils.contains(notNullField.grant(), grantType) && (field.get(param) == null
            || StringUtils.isBlank(field.get(param).toString()))) {
          return resultHelper
              .infoResp(ErrorType.SYS0002, notNullField.message(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
      } catch (IllegalAccessException ignore) {
      }
      if (field.isAnnotationPresent(SizeField.class)) {
        SizeField size = field.getAnnotation(SizeField.class);
        try {
          if (ArrayUtils.contains(size.grant(), grantType)
              && (field.get(param).toString().length() > size.max()
              || field.get(param).toString().length() < size.min())) {
            return resultHelper
                .infoResp(ErrorType.SYS0002, size.message(), HttpStatus.UNPROCESSABLE_ENTITY);
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
    return Mono.empty();
  }

  private final ResultHelper resultHelper;

  @Autowired public ValidateHelper(ResultHelper resultHelper) {
    this.resultHelper = resultHelper;
  }
}
