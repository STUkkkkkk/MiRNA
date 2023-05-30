package mirna.stukk.Pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-30 0:38
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MirnaRelationDownload {
    @ApiModelProperty("需要查询的mirna")
    private List<String> mirnas;
    @ApiModelProperty("需要查询的disease")
    private List<String> diseases;
    @ApiModelProperty("需要查询的数据来源类型")
    private Integer resource;
    @ApiModelProperty("需要查询的最小关联性")
    private Double minRelevance;
    @ApiModelProperty("需要查询的最大的关联性")
    private Double maxRelevance;
    @ApiModelProperty("查询的行数")
    private Integer row;
    @ApiModelProperty("下载类型，是0:xlsx----1:csv----2:txt----3:json")
    private Integer downloadType;
}
