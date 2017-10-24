package com.saintdan.framework.filter;

import com.saintdan.framework.config.bean.RequestBean;
import com.saintdan.framework.constant.CommonsConstant;
import com.saintdan.framework.tools.RemoteAddressUtils;
import java.util.HashMap;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * Limit filter.
 * <pre>
 *   I use map as an cache in this case.
 *   You can also use redis.
 * </pre>
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 23/10/2017
 * @since JDK1.8
 */
@Component
public class LimitFilter implements WebFilter {

  @Override public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    ServerHttpRequest request = exchange.getRequest();
    final String LIMIT_KEY = "Limit-Key";
    List<String> limitKeyHeaders = request.getHeaders().get(LIMIT_KEY);
    if (!CollectionUtils.isEmpty(limitKeyHeaders) && StringUtils.isNotBlank(limitKeyHeaders.get(0))
        && !limit(new RequestLimit(RemoteAddressUtils.getRealIp(request),
        request.getURI().getPath(),
        limitKeyHeaders.get(0),
        requestBean.getRange(),
        requestBean.getCount()))) {
      exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
      return Mono.empty();
    }
    return chain.filter(exchange);
  }

  private boolean limit(RequestLimit requestLimit) {
    String key = String
        .join(CommonsConstant.UNDERLINE, requestLimit.getIp(), requestLimit.getPath(),
            requestLimit.getLimitKey());
    if (!map.containsKey(key)) {
      map.put(key, new RequestCount(key, 1, System.currentTimeMillis()));
    } else {
      RequestCount requestCount = map.get(key);
      long frequency = (System.currentTimeMillis() - requestCount.getFirstReqAt());
      if (frequency > requestLimit.getRange()) {
        map.remove(key);
      } else {
        if (requestCount.getCount() >= requestLimit.getCount() && frequency <= requestLimit
            .getRange()) {
          return false;
        } else {
          requestCount.setCount(requestCount.getCount() + 1);
          map.remove(key);
          map.put(key, requestCount);
        }
      }
    }
    return true;
  }

  private HashMap<String, RequestCount> map = new HashMap<>();
  private final RequestBean requestBean;

  @Autowired public LimitFilter(RequestBean requestBean) {
    this.requestBean = requestBean;
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private class RequestLimit {

    private String ip; // Request ip
    private String path; // Request resource's path
    private String limitKey; // Key of limit
    private long range; // Millisecond
    private int count; // Request count
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  private class RequestCount {

    private String key;
    private int count;
    private Long firstReqAt;
  }
}
