package com.saintdan.framework.enums;

import com.saintdan.framework.constant.ResourcePath;
import com.saintdan.framework.param.UserParam;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 20/10/2017
 * @since JDK1.8
 */
public enum ResourceUri {

  USERS(ResourcePath.USERS, UserParam.class),

  UNKNOWN("unknown", null);

  private String uri;
  private Class clazz;

  private static final Map<String, ResourceUri> mappings = new HashMap<>(2);

  static {
    for (ResourceUri resourceUri : values()) {
      mappings.put(resourceUri.uri, resourceUri);
    }
  }

  ResourceUri(String uri, Class clazz) {
    this.uri = uri;
    this.clazz = clazz;
  }

  public String uri() {
    return uri;
  }

  public Class clazz() {
    return clazz;
  }

  public static ResourceUri resolve(String uri) {
    String matchUri = mappings.keySet().stream()
        .filter(uri::contains)
        .findFirst()
        .orElse(null);
    return (matchUri != null ? mappings.get(matchUri) : UNKNOWN);
  }

  public boolean matches(String uri) {
    return (this == resolve(uri));
  }

  }
