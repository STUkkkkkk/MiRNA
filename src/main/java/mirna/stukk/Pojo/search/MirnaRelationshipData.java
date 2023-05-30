package mirna.stukk.Pojo.search;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mirna.stukk.Pojo.DTO.MirnaRelationDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-29 19:52
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MirnaRelationshipData implements Serializable {
    private List<MirnaRelationDTO> mirnaRelationDTOList;
    private Integer pageNum;
    private Integer pageSize;
    private Integer total;

}
