package mirna.stukk.utils;

import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-25 17:00
 **/
public class ArticleUtils {

    public static List<ArticleDTO> ArticleListToDto(List<Article>articleList){
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for(Article article : articleList){
            articleDTOList.add(ArticleToDto(article));
        }
        return articleDTOList;
    }

    public static ArticleDTO ArticleToDto(Article article){
        return ArticleDTO.builder()
                .pmid(article.getPmid())
                .title(article.getTitle())
                .authors(StringToListUtils.StringToList(article.getAuthors()))
                .types(StringToListUtils.StringToList(article.getType()))
                .keywords(StringToListUtils.StringToList(article.getKeywords()))
                .doi(DoiUtils.ToDoi(article.getDoi()))
                .url(DoiUtils.ToUrl(article.getDoi()))
                .library(article.getLibrary())
                .abs(article.getAbs())
                .date(article.getDate())
                .build();
    }

}
