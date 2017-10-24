package com.saintdan.framework.component;

import com.saintdan.framework.enums.ErrorType;
import com.saintdan.framework.tools.LogUtils;
import com.saintdan.framework.vo.ErrorVO;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Log debug message and return result.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 9/22/15
 * @since JDK1.8
 */
@Component
public class ResultHelper {

  /**
   * Return error information.
   *
   * @param errorType error type
   * @return response entity with information.
   */
  public Mono<ServerResponse> infoResp(ErrorType errorType, HttpStatus httpStatus) {
    return infoResp(errorType, errorType.description(), httpStatus);
  }

  /**
   * Return error information.
   *
   * @param errorType error type
   * @param msg error message
   * @return response entity with information.
   */
  @SuppressWarnings("unchecked") public Mono<ServerResponse> infoResp(ErrorType errorType, String msg, HttpStatus httpStatus) {
    return ServerResponse.status(httpStatus)
        .body(Mono.just(new ErrorVO(errorType.name(), msg)), ErrorVO.class);
  }

  /**
   * Return error information.
   *
   * @param logger Log
   * @param msg error message
   * @return response entity with information.
   */
  public Mono<ServerResponse> infoResp(Logger logger, ErrorType errorType, String msg,
      HttpStatus httpStatus) {
    LogUtils.trackInfo(logger, msg);
    return ServerResponse.status(httpStatus)
        .body(Mono.just(new ErrorVO(errorType.name(), msg)), ErrorVO.class);
  }

  /**
   * Return error information, and log it to error.
   *
   * @param logger Log
   * @param errorType error type
   * @param e e
   * @return response entity with error message.
   */
  public Mono<ServerResponse> errorResp(Logger logger, Throwable e, ErrorType errorType,
      HttpStatus httpStatus) {
    return errorResp(logger, e, errorType, errorType.description(), httpStatus);
  }

  /**
   * Return error information, and log it to error.
   *
   * @param logger Log
   * @param errorType error type
   * @param e e
   * @param msg error message
   * @return response entity with error message.
   */
  public Mono<ServerResponse> errorResp(Logger logger, Throwable e, ErrorType errorType, String msg,
      HttpStatus httpStatus) {
    LogUtils.traceError(logger, e, errorType.description());
    return ServerResponse.status(httpStatus)
        .body(Mono.just(new ErrorVO(errorType.name(), msg)), ErrorVO.class);
  }

}
