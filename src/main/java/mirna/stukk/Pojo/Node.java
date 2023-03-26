package mirna.stukk.Pojo;

import io.swagger.annotations.Api;
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
@ApiModel("关系图的节点实体类")
public class Node implements Serializable { //这个是关系图的点
    @ApiModelProperty("节点编号")
    private String id;//节点的编号
    @ApiModelProperty("结点的名字")
    private String name;//结点的名字
    @ApiModelProperty("节点的大小")
    private Integer symbolSize;//大小
    @ApiModelProperty("节点的类型")
    private Integer category;// 类型
    @ApiModelProperty("节点的权重")
    private double value; //权重
}
