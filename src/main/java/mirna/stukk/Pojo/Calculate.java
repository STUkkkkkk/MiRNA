package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-28 20:09
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("tb_prediction")
@ApiModel("预测的实体类")
public class Calculate {

    private String mirna;
    private String disease;
    private Integer proved;
    private Double forecastRelevance;

}
