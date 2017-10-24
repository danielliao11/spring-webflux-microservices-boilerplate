package com.saintdan.framework.tools;

import com.saintdan.framework.constant.CommonsConstant;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.CollectionUtils;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 27/12/2016
 * @since JDK1.8
 */
public class RemoteAddressUtils {

  public static String getRealIp(ServerHttpRequest request) {
    final String X_FORWARDED_HEADER = "X-Forwarded-For";
    final String X_REAL_HEADER = "X-Real-IP";
    final String UNKNOWN = "unknown";
    final List<String> forwardedHeaders = request.getHeaders().get(X_FORWARDED_HEADER);
    String ip = CollectionUtils.isEmpty(forwardedHeaders) ? CommonsConstant.BLANK : forwardedHeaders.get(0);
    if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
      int index = ip.indexOf(CommonsConstant.COMMA);
      if (index != -1) {
        return ip.substring(0, index);
      } else {
        return ip;
      }
    }
    final List<String> realHeaders = request.getHeaders().get(X_REAL_HEADER);
    ip = CollectionUtils.isEmpty(realHeaders) ? CommonsConstant.BLANK : realHeaders.get(0);
    if (StringUtils.isNotEmpty(ip) && !UNKNOWN.equalsIgnoreCase(ip)) {
      return ip;
    }
    return request.getRemoteAddress() == null ? CommonsConstant.BLANK : request.getRemoteAddress().toString();
  }
}
