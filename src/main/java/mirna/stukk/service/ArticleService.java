package mirna.stukk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.SearchResult;
import mirna.stukk.config.Result;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ArticleService extends IService<Article> {
    public List<Article> getByLimit(Integer pageNum,Integer pageSize);

    Result<SearchResult> getByLike(String message, Integer pageNum, Integer pageSize) throws Exception;

    Result<ArticleDTO> getByPmid(Long pmid)  throws IOException;
}
