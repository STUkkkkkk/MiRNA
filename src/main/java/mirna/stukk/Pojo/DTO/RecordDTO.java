package mirna.stukk.Pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-01 0:42
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDTO {

    private String searchName;
    private Long times;

}
