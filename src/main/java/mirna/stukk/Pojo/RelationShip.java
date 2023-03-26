package mirna.stukk.Pojo;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(tags = "mirnaName-diseaseName-pmid的关系实体类")
@TableName("relationship")
public class RelationShip implements Serializable {

    private Long pmid;
    @TableField(value = "mirna_name")
    private String mirnaName;

    @TableField(value = "disease_name")
    private String diseaseName;

}
