package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @DateTime: 2023-05-26 23:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "表示preMicroRNA的")
public class Mirna_3P_5P implements Serializable {
    private static final long SerialVersionUID = 1L;
    @ApiModelProperty(value = "mirna名字")
    private String mirnaName;
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
    @ApiModelProperty(value = "CDNA")
    private String cdna; //？？？
    @ApiModelProperty(value = "microRNA的版本")
    private String microRNAs = "miRBase v22";
    @ApiModelProperty(value = "版本")
    private String genomeBuildId = "GRCh38";
    @ApiModelProperty(value = "版本")
    private String genomeBuildAccession = "NCBI_Assembly:GCA_000001405.15";
}
