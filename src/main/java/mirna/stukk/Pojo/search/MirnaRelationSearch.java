package mirna.stukk.Pojo.search;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-29 16:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "mirna和疾病等的关联数据接收查询得到实体类")
public class MirnaRelationSearch {
    @ApiModelProperty("需要查询的mirna")
    private List<String> mirnas;
    @ApiModelProperty("需要查询的disease")
    private List<String> diseases;
    @ApiModelProperty("当前页面数量")
    private Integer pageNum;
    @ApiModelProperty("页面数据量大小")
    private Integer pageSize;
    @ApiModelProperty("需要查询的数据来源类型")
    private Integer resource;
    @ApiModelProperty("需要查询的最小关联性")
    private Double minRelevance;
    @ApiModelProperty("需要查询的最大的关联性")
    private Double maxRelevance;
}
