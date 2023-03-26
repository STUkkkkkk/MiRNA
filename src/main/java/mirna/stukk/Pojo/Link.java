package mirna.stukk.Pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value = "边的实体类")
public class Link implements Serializable {


    @ApiModelProperty("起点的id")
    private String source;

    @ApiModelProperty("终点的节点")
    private String target;

}
