package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-26 23:27
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("mirna_all_message")
@ApiModel(value = "mirna很多信息的实体类")
public class MirnaAllMessage implements Serializable {
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;
    @ApiModelProperty(value = "mirna名字")
    private String mirnaName;
    @ApiModelProperty(value = "mirna图片格式---都是svg")
    private String url;
    @ApiModelProperty(value = "貌似是编号...")
    private String accession; //编号？
    @ApiModelProperty(value = "属于的mirna家族")
    private String family;
    @ApiModelProperty(value = "种类？")
    private String species; //？？？
    @ApiModelProperty(value = "序列长度")
    private Long length; //长度
    @ApiModelProperty(value = "基因序列")
    private String sequence; // 基因序列
    @TableField(value = "CDNA")
    @ApiModelProperty(value = "CDNA")
    private String cdna; //？？？
    @TableField(exist = false)
    @ApiModelProperty(value = "microRNA的版本")
    private String microRNAs = "miRBase v22";
    @TableField(exist = false)
    @ApiModelProperty(value = "版本")
    private String genomeBuildId = "GRCh38";
    @TableField(exist = false)
    @ApiModelProperty(value = "版本")
    private String genomeBuildAccession = "NCBI_Assembly:GCA_000001405.15";
    @TableField(exist = false)
    @ApiModelProperty(value = "2D结构链接")
    private String StructUrl_2D;
    @TableField(exist = false)
    @ApiModelProperty(value = "3D结构链接")
    private String StructUrl_3D;
    @TableField(exist = false)
    @ApiModelProperty(value = "3p的信息")
    private Mirna_3P_5P mirna_3P;
    @TableField(exist = false)
    @ApiModelProperty(value = "5p的信息")
    private Mirna_3P_5P mirna_5P;
    @TableField(exist = false)
    private static final long SerialVersionUID = 1L;

}
