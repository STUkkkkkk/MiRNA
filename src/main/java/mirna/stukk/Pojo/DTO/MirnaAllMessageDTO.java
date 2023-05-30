package mirna.stukk.Pojo.DTO;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mirna.stukk.Pojo.Mirna_3P_5P;

import java.io.Serializable;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-28 18:21
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "mirna很多信息的实体类DTO")
public class MirnaAllMessageDTO implements Serializable {
    @ApiModelProperty(value = "mirna图片格式---都是svg")
    private String url;
    @ApiModelProperty(value = "主mirna的信息(左边)")
    private Mirna_3P_5P mainMirna;
    @ApiModelProperty(value = "2D结构链接")
    private String StructUrl_2D;
    @ApiModelProperty(value = "3D结构链接")
    private String StructUrl_3D;
    @ApiModelProperty(value = "3p的信息")
    private Mirna_3P_5P mirna_3P;
    @ApiModelProperty(value = "5p的信息")
    private Mirna_3P_5P mirna_5P;
    private static final long SerialVersionUID = 1L;

}
