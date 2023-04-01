package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @DateTime: 2023-03-29 22:10
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName(value = "gene_mirna")
@ApiModel(value = "关于基因和mirna的关系")
public class GeneMirnaRelationship implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "基因")
    private String gene;
    @ApiModelProperty(value = "基因名字")
    @TableField(value = "mirna_name")
    private String mirnaName;
    @ApiModelProperty(value = "出版物，maybe代表这个关系自哪里吧...")
    private String publication;
    @ApiModelProperty(value = "类型，方式")
    private String methods;
    @ApiModelProperty(value = "组织")
    private String tissue;
    @ApiModelProperty(value = "细胞系")
    @TableField(value = "cell_line")
    private String cellLine;




}
