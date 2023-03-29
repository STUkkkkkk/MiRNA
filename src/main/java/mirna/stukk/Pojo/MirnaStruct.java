package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 14:46
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "mirna_struct")
public class MirnaStruct {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField(value = "mirna_name")
    @ApiModelProperty(value = "mirna名字")
    private String mirnaName;
    @ApiModelProperty(value = "结构图第1行")
    private String first;
    @ApiModelProperty(value = "结构图第2行")
    private String second;
    @ApiModelProperty(value = "结构图第3行")
    private String third;
    @ApiModelProperty(value = "结构图第4行")
    private String fourth;
    @ApiModelProperty(value = "结构图第5行")
    private String fifth;


}
