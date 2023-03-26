package mirna.stukk.Pojo;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import mirna.stukk.Pojo.DTO.ArticleDTO;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Api(tags = "精准查询某个mirna和disease的文章")
public class SearchResult implements Serializable {

    private int count;

    private List<ArticleDTO> articles;

}
