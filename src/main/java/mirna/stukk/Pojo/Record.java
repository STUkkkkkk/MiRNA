package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-01 0:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("搜索记录的实体类")
@TableName(value = "record")
public class Record implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty("id")
    private Long id;

    @TableField(value = "search_name")
    @ApiModelProperty("搜索的名字")
    private String searchName;

    @ApiModelProperty("搜索次数")
    private Long times;

}
