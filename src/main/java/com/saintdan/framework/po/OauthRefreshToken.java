package com.saintdan.framework.po;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 26/10/2016
 * @since JDK1.8
 */
@Data public class OauthRefreshToken {
  @Id private String tokenId;
}
