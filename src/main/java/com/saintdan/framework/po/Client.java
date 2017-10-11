package com.saintdan.framework.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Authorized client, provide for spring security.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 10/23/15
 * @since JDK1.8
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Client {
  private String id;
  private String clientIdAlias;
  private String resourceIdStr;
  private String clientSecretAlias;

  /**
   * Available values: read, write
   */
  private String scopeStr;

  /**
   * grant types include
   * "authorization_code", "password", "assertion", and "refresh_token".
   * Default description is "authorization_code,refresh_token".
   */
  private String authorizedGrantTypeStr;

  /**
   * The redirect URI(s) established during registration (optional, comma separated).
   */
  private String registeredRedirectUriStr;

  /**
   * Authorities that are granted to the client (comma-separated). Distinct from the authorities
   * granted to the user on behalf of whom the client is acting.
   * <pre>
   *     For example: USER
   * </pre>
   */
  private String authoritiesStr;

  /**
   * The access token validity period in seconds (optional).
   * If unspecified a global default will be applied by the token services.
   */
  @Builder.Default
  private int accessTokenValiditySecondsAlias = 1800;

  /**
   * The refresh token validity period in seconds (optional).
   * If unspecified a global default will  be applied by the token services.
   */
  @Builder.Default
  private int refreshTokenValiditySecondsAlias = 3600;

  /**
   * Additional information for this client, not needed by the vanilla OAuth protocol but might be useful, for example,
   * for storing descriptive information.
   */
  private String additionalInformationStr;
  private long createdAt;
  private long createdBy;
  private long lastModifiedAt;
  private long lastModifiedBy;
  private String publicKey;

//  @Override public String getClientId() {
//    return getClientIdAlias();
//  }
//
//  @Override public Set<String> getResourceIds() {
//    return str2Set(getResourceIdStr());
//  }
//
//  @Override public boolean isSecretRequired() {
//    return true;
//  }
//
//  @Override public String getClientSecret() {
//    return getClientSecretAlias();
//  }
//
//  @Override public boolean isScoped() {
//    return true;
//  }
//
//  @Override public Set<String> getScope() {
//    return str2Set(getScopeStr());
//  }
//
//  @Override public Set<String> getAuthorizedGrantTypes() {
//    return str2Set(getAuthorizedGrantTypeStr());
//  }
//
//  @Override public Set<String> getRegisteredRedirectUri() {
//    return str2Set(getRegisteredRedirectUriStr());
//  }
//
//  @Override public Collection<GrantedAuthority> getAuthorities() {
//    return Arrays.stream(getAuthorizedGrantTypeStr().split(CommonsConstant.COMMA))
//        .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//  }
//
//  @Override public Integer getAccessTokenValiditySeconds() {
//    return getAccessTokenValiditySecondsAlias();
//  }
//
//  @Override public Integer getRefreshTokenValiditySeconds() {
//    return null;
//  }
//
//  @Override public boolean isAutoApprove(String scope) {
//    return false;
//  }
//
//  @Override public Map<String, Object> getAdditionalInformation() {
//    return null;
//  }
//
//  private Set<String> str2Set(String str) {
//    if (StringUtils.isBlank(str)) {
//      return new HashSet<>();
//    }
//    return Sets.newHashSet(Arrays.stream(str.split(CommonsConstant.COMMA)).collect(Collectors.toList()));
//  }
}
