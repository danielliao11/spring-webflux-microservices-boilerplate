package com.saintdan.framework.filter;

import com.saintdan.framework.annotation.NotNullField;
import com.saintdan.framework.annotation.SizeField;
import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.vo.ErrorVO;
import java.lang.reflect.Field;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Mono;

/**
 * Validate param field.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Component
public class ValidateFilter {

  /**
   * Bean properties null validation.
   *
   * @param param Param bean
   * @param method {@link HttpMethod}
   * @return error
   */
  public Mono<ErrorVO> validate(Object param, HttpMethod method) {
    Field[] fields = param.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (field == null || !field.isAnnotationPresent(NotNullField.class)) {
        continue; // Ignore field without ParamField annotation.
      }
      field.setAccessible(true);
      NotNullField notNullField = field.getAnnotation(NotNullField.class);
      try {
        if (ArrayUtils.contains(notNullField.value(), method) && ObjectUtils.isEmpty(field.get(param))) {
          return Mono.just(ErrorVO.builder().error(ErrorType.SYS0002.name()).error_description(notNullField.message()).build());
        }
      } catch (IllegalAccessException ignore) {
      }
      if (field.isAnnotationPresent(SizeField.class)) {
        SizeField size = field.getAnnotation(SizeField.class);
        try {
          if (ArrayUtils.contains(size.value(), method)
              && (field.get(param).toString().length() > size.max()
              || field.get(param).toString().length() < size.min())) {
            return Mono.just(ErrorVO.builder().error(ErrorType.SYS0002.name()).error_description(notNullField.message()).build());
          }
        } catch (IllegalAccessException ignore) {
        }
      }
    }
    return Mono.empty();
  }
}
