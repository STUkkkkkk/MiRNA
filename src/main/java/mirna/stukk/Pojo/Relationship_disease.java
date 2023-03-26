package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("relationship_disease")
public class Relationship_disease implements Serializable {

    private Long pmid;

    @TableField("disease_name")
    private String diseaseName;
}
