package com.saintdan.framework.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.util.CollectionUtils;

/**
 * Authorized users, provide for spring security oauth2.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 6/23/15
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"authoritySet", "accounts"})
@ToString(exclude = {"authoritySet", "accounts"})
public class User {

  @Id private String id;

  private String name;

  private String usr;

  @JsonIgnore
  private String pwd;

  @Builder.Default
  @JsonIgnore
  private boolean isAccountNonExpiredAlias = true;

  @Builder.Default
  @JsonIgnore
  private boolean isAccountNonLockedAlias = true;

  @Builder.Default
  @JsonIgnore
  private boolean isCredentialsNonExpiredAlias = true;

  @Builder.Default
  @JsonIgnore
  private boolean isEnabledAlias = true;

  private String description;

  private long lastLoginAt;

  private String ip;

  @Builder.Default
  private long createdAt = System.currentTimeMillis();

  private long createdBy;

  @Builder.Default
  private long lastModifiedAt = System.currentTimeMillis();

  private long lastModifiedBy;

  private String role;

  @JsonIgnore
  private Set<Account> accounts;

  @JsonIgnore
  private Set<String> authoritySet;

  /**
   * Get the authorities.
   *
   * @return GrantedAuthorities
   */
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return CollectionUtils.isEmpty(authoritySet) ? new HashSet<>() : authoritySet.stream()
        .map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
  }

}
