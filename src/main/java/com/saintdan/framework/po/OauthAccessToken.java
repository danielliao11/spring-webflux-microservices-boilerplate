package com.saintdan.framework.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * Oauth access token.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 26/10/2016
 * @since JDK1.8
 */
@Data
public class OauthAccessToken {

  @Id private String tokenId;
  private String userName;
  private String clientId;
}
