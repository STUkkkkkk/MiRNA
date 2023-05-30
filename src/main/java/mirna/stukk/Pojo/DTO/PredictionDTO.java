package mirna.stukk.Pojo.DTO;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.spatial3d.geom.SerializableObject;

import javax.annotation.security.DenyAll;
import java.io.Serializable;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-29 17:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PredictionDTO implements Serializable {
    private String mirna;
    private String disease;
    private Integer proved;
    private Double forecastRelevance;
}
