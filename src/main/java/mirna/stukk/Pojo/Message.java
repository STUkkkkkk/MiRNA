package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-27 20:44
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "tb_message")
@ApiModel(value = "用户留言信息类")
public class Message {
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id ,前端直接不管他，我在后端会初始化")
    private Long id;
    @ApiModelProperty(value = "用户名称")
    private String name;

    @ApiModelProperty(value = "用户的email")
    private String email;

    @ApiModelProperty(value = "用户的留言信息")
    private String message;

    @ApiModelProperty(value = "时间，前端直接不管他，我在后端会初始化")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

}
