package mirna.stukk.Pojo.DTO;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-19 12:34
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO implements Serializable {

    private Long pmid;

    private String title;
    private List<String> types;
    private List<String> authors;
    private List<String> keywords;
    private String doi;
    private String url;
    private String library;
    private String abs;
    private String date;
}
