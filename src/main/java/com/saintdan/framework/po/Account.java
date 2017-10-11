package com.saintdan.framework.po;

import com.saintdan.framework.enums.AccountSourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * Account of user.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 08/02/2017
 * @since JDK1.8
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Account {
  @Id private String id;
  private String account;
  private AccountSourceType accountSourceType;
  private long createdAt;
  private long createdBy;
  private long lastModifiedAt;
  private long lastModifiedBy;
  private long userId;
}
