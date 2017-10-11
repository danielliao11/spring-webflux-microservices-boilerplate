package com.saintdan.framework.po;

import com.saintdan.framework.enums.ValidFlag;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

/**
 * Authorized users, provide for spring security oauth2.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 6/23/15
 * @since JDK1.8
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(exclude = {"authoritySet", "accounts"}) @ToString(exclude = {"authoritySet", "accounts"})
public class User {
  @Id private String id;
  private String name;
  private String usr;
  private String pwd;
  @Builder.Default
  private boolean isAccountNonExpiredAlias = true;
  @Builder.Default
  private boolean isAccountNonLockedAlias = true;
  @Builder.Default
  private boolean isCredentialsNonExpiredAlias = true;
  @Builder.Default
  private boolean isEnabledAlias = true;
  @Builder.Default
  private ValidFlag validFlag = ValidFlag.VALID;
  private String description;
  private long lastLoginAt;
  private String ip;
  private long createdAt;
  private long createdBy;
  private long lastModifiedAt;
  private long lastModifiedBy;
  private Set<Account> accounts;
  private Set<String> authoritySet;

//  /**
//   * Get the authorities.
//   *
//   * @return GrantedAuthorities
//   */
//  @Override public Collection<? extends GrantedAuthority> getAuthorities() {
//    Collection<GrantedAuthority> authorities = new ArrayList<>();
//    getAuthoritySet()
//        .forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));
//    return authorities;
//  }
//
//  @Override public String getUsername() {
//    return getUsr();
//  }
//
//  @Override public String getPassword() {
//    return getPwd();
//  }
//
//  @Override public boolean isAccountNonExpired() {
//    return isAccountNonExpiredAlias();
//  }
//
//  @Override public boolean isAccountNonLocked() {
//    return isAccountNonLockedAlias();
//  }
//
//  @Override public boolean isCredentialsNonExpired() {
//    return isCredentialsNonExpiredAlias();
//  }
//
//  @Override public boolean isEnabled() {
//    return isEnabledAlias();
//  }
}
