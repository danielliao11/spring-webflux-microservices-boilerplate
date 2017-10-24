package com.saintdan.framework.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result VO.
 *
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 8/19/15
 * @since JDK1.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorVO {

  private String error;
  private String error_description;
}
