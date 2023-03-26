package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
@Builder
@TableName("prediction")
public class Prediction implements Serializable {

    private Long mirnaId;
    private Long diseaseId;
    private int proved;

    @TableField(exist = false)
    private String name;

    private double forecastRelevance;

}
