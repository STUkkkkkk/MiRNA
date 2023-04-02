package mirna.stukk.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.SearchResult;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.ArticleMapper;
import mirna.stukk.service.ArticleService;
import mirna.stukk.utils.ArticleUtils;
import mirna.stukk.utils.DoiUtils;
import mirna.stukk.utils.StringToListUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private RestHighLevelClient restHighLevelClient;


    @Override
    public List<Article> getByLimit(Integer pageNum, Integer pageSize) {
        int from = (pageNum - 1)*pageSize;
        int size = pageSize;
        List<Article> k_articles = articleMapper.get_1k_articles(from, size);
        return k_articles;
    }

    @Override
    public Result<SearchResult> getByLike(String message, Integer pageNum, Integer pageSize) throws Exception {
        SearchRequest searchRequest = new SearchRequest("article"); //构建查询请求
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.multiMatchQuery(message, "title","abs"));
        HighlightBuilder highlightBuilder = new HighlightBuilder();// 设置高亮
        HighlightBuilder.Field titleLight = new HighlightBuilder.Field("title"); //设置标题高亮
        titleLight.preTags("<em class = 'TitleLight'>");
        titleLight.postTags("</em>");
        //设置高亮 ， 标题

        HighlightBuilder.Field abstractLight = new HighlightBuilder.Field("abs"); //设置标题高亮
        abstractLight.preTags("<em class = 'AbstractLight'>");
        abstractLight.postTags("</em>"); //设置简介的高亮


        HighlightBuilder.Field pmidLight = new HighlightBuilder.Field("abs"); //设置标题高亮
        abstractLight.preTags("<em class = 'AbstractLight'>");
        abstractLight.postTags("</em>"); //设置简介的高亮


        highlightBuilder.field(titleLight);
        highlightBuilder.field(abstractLight);

        highlightBuilder.fragmentSize(800000);//设置高亮的最大分片数
        highlightBuilder.numOfFragments(0);  //设置从第一个分片开始

        searchSourceBuilder.highlighter(highlightBuilder);
        searchSourceBuilder.from((pageNum - 1) * pageSize);
        searchSourceBuilder.size(pageSize);
        searchSourceBuilder.trackTotalHits(true);
        searchRequest.source(searchSourceBuilder);
        SearchResponse search = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        if(search == null || search.getHits() == null || search.getHits().getTotalHits() == null || search.getHits().getHits() == null){
            return Result.error("511","查询失败");
        }
        int count = (int) search.getHits().getTotalHits().value; //获取所有的关系数量
        SearchHit[] hits = search.getHits().getHits();
        List<ArticleDTO> articleDTOList = new LinkedList<>();
        for (SearchHit searchHit : hits){
            Map<String, HighlightField> highlightFields =searchHit.getHighlightFields();
            Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
            Long pmid = Long.parseLong(sourceAsMap.get("pmid").toString());
//            这个是标题的内容
            String title = null;
            if(highlightFields.get("title") != null){
                Text[] titles = highlightFields.get("title").getFragments();
                String s = "";
                for(int i = 0;i<titles.length;i++){
                    s += titles[i].toString();
                }
                title = s;
            }
            else{
                if(sourceAsMap.get("title") != null ){
                    title = sourceAsMap.get("title").toString();
                }
            }
//            这是简介的信息
            String abs = null;
            if(highlightFields.get("abs") != null){
                Text[] abs1 = highlightFields.get("abs").getFragments();
                String s = "";
                for(int i = 0;i<abs1.length;i++){
                    s += abs1[i].toString();
                }
                abs = s;
            }
            else{
                if(sourceAsMap.get("abs") != null ){
                    abs = sourceAsMap.get("abs").toString();
                }
            }
//          这是date日期的信息
            String date = sourceAsMap.get("date") == null ? null : sourceAsMap.get("date").toString();
//          这是作者的信息
            List<String> authors = StringToListUtils.StringToList(sourceAsMap.get("authors") == null ? null : sourceAsMap.get("authors").toString());
//          这是论文doi的信息
            String doi = DoiUtils.ToDoi(sourceAsMap.get("doi") == null ? null : sourceAsMap.get("doi").toString());
//          这是url
            String url = DoiUtils.ToUrl(sourceAsMap.get("doi") == null ? null : sourceAsMap.get("doi").toString());
//          这是图书馆
            String library = sourceAsMap.get("library") == null ? null : sourceAsMap.get("library").toString();
//          这是类型
            List<String> types = StringToListUtils.StringToList(sourceAsMap.get("type") == null ? null : sourceAsMap.get("type").toString());
//          这是关键字
            List<String> keywords = StringToListUtils.StringToList(sourceAsMap.get("keywords") == null ? null : sourceAsMap.get("keywords").toString());
            articleDTOList.add(ArticleDTO.builder()
                    .title(title)
                    .abs(abs)
                    .types(types)
                    .library(library)
                    .date(date)
                    .pmid(pmid)
                    .doi(doi)
                    .keywords(keywords)
                    .authors(authors)
                    .url(url)
                    .build());
        }

        return Result.success(SearchResult.builder().count(count).articles(articleDTOList).build());
    }



    @Override
    public Result<ArticleDTO> getByPmid(Long pmid) {
        Article article = this.getById(pmid);
        return Result.success(ArticleUtils.ArticleToDto(article));
    }

    @Override
    public List<Article> getByPmids(List<Long> pmids) {
        List<Article> articles = articleMapper.selectBatchIds(pmids);
        return articles;
    }
}
