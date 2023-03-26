package mirna.stukk.Pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel("关系图的实体类")
public class Diagram implements Serializable {

    @ApiModelProperty("节点")
    private List<Node> nodes;

    @ApiModelProperty("边")
    private List<Link> links;

    @ApiModelProperty("类型")
    private List<Category> categories;

}
