package mirna.stukk.Pojo.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-29 16:14
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MirnaRelationDTO implements Serializable {
    private String mirnaName;
    private String disease;
    private String resource = "Human microRNA Disease DataBase(HMDD)";
    private Long pmid;
    private Double relevance;
}
