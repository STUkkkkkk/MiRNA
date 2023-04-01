package mirna.stukk.Pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-30 14:35
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("mirna-基因关系图的实体类")
public class GeneDiagram implements Serializable {

    @ApiModelProperty("节点")
    private List<GeneNode> geneNodes;

    @ApiModelProperty("边")
    private List<Link> links;

    @ApiModelProperty("类型")
    private List<Category> categories;

}
