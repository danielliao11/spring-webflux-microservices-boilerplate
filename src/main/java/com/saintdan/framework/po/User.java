package com.saintdan.framework.po;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 09/10/2017
 * @since JDK1.8
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class User implements Serializable {

  private static final long serialVersionUID = 6605960539746548526L;
  private long id;
  private String name;
}
