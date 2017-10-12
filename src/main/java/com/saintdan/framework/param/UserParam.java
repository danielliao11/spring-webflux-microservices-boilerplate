package com.saintdan.framework.param;

import com.saintdan.framework.annotation.NotNullField;
import com.saintdan.framework.annotation.SizeField;
import com.saintdan.framework.enums.OperationType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author <a href="http://github.com/saintdan">Liao Yifan</a>
 * @date 11/10/2017
 * @since JDK1.8
 */
@Data public class UserParam {

  @ApiModelProperty(value = "username", required = true, notes = "usr must greater than or equal to 4 and less than or equal to 50.")
  @NotNullField(value = OperationType.CREATE, message = "usr cannot be null.")
  @SizeField(min = 4, max = 50, value = OperationType.CREATE, message = "usr must greater than or equal to 4 and less than or equal to 50.")
  private String usr; // username

  @ApiModelProperty(value = "password", required = true, notes = "pwd must greater than or equal to 4 and less than or equal to 16.")
  @NotNullField(value = OperationType.CREATE, message = "pwd cannot be null.")
  @SizeField(min = 4, max = 16, value = OperationType.CREATE, message = "pwd must greater than or equal to 4 and less than or equal to 16.")
  private String pwd; // password

  @ApiModelProperty(value = "user's name")
  private String name; // user's name

  private String description;

}
