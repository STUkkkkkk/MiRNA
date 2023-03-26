package mirna.stukk.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mirna.stukk.Pojo.*;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.SearchResultMapper;
import mirna.stukk.service.*;
import mirna.stukk.utils.ArticleUtils;
import mirna.stukk.utils.DoiUtils;
import mirna.stukk.utils.KMPutils;
import mirna.stukk.utils.StringToListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SearchResultServiceImpl implements SearchResultService {

    @Autowired
    private SearchResultMapper searchResultMapper;

    @Autowired
    private MirnaService mirnaService;
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DiseaseService diseaseService;
    public static boolean isYear(String num){
        char a[] = num.toCharArray();
        for(int i = 0;i<a.length;i++){
            if(!Character.isDigit(a[i])){
                return false;
            }
        }
        return true;
    }

    public void highLight(List<Article> articles){ //高亮
        List<Long> pmids = articles.stream().map(Article::getPmid).collect(Collectors.toList());
        List keyWords = redisTemplate.opsForHash().multiGet("articles", pmids);
        log.info("关键词为：{}",keyWords);
        for(int i = 0;i<keyWords.size();i++){
            if(keyWords.get(i) == null){
                continue;
            }
            Article article = articles.get(i);
            String keyWord = (String) keyWords.get(i);
            String abs = article.getAbs();
            List<String> keywordList = (List<String>) JSON.parse(keyWord);
            for(String s : keywordList){
                abs = KMPutils.HighLight(abs, s);
            }
            article.setAbs(abs);
        }
    }

    @Override
    public Result<SearchResult> getByDisease(Integer pageNum, String diseaseName,String startTime,String endTime,Integer pageSize) {
        int num1 = (pageNum - 1)*pageSize;
        String st = null,et = null;
        if(isYear(startTime)){
            st= startTime;
        }
        if (isYear(endTime)) {
            et = Integer.parseInt(endTime) + 1+"";
        }
        Disease disease_name = diseaseService.query().eq("disease_name", diseaseName).one();
        if(disease_name == null){
            return Result.success(new SearchResult());
        }
        List<Object> byDisease = searchResultMapper.getByDisease(num1, diseaseName, st, et,pageSize);
        if(byDisease == null){
            return Result.success(null);
        }
        Integer num = ((List<Integer>)byDisease.get(1)).get(0);
        List<Article> articles = (List<Article>) byDisease.get(0);
        highLight(articles);


        SearchResult searchDisease =  new SearchResult(num, ArticleUtils.ArticleListToDto(articles));
        return Result.success(searchDisease);
    }

    @Override
    public Result<SearchResult> getByMirna(Integer pageNum, String mirnaName, String startTime, String endTime,Integer pageSize) {
        int num1 = (pageNum - 1)*pageSize;
        String st = null,et = null;
        if(isYear(startTime)){
            st= startTime;
        }
        if (isYear(endTime)) {
            et = Integer.parseInt(endTime) + 1+"";
        }
        MiRNA mirna_name = mirnaService.query().eq("mirna_name", mirnaName).one();
        if(mirna_name == null){
            //如果用户输入的值是空或者无效的值
            return Result.success(new SearchResult());
        }
        List<Object> byMirna = searchResultMapper.getByMirna(num1, mirnaName, st, et,pageSize);
        if(byMirna == null){
            return Result.success(null);
        }
        Integer num = ((List<Integer>)byMirna.get(1)).get(0);
        List<Article> articles = (List<Article>) byMirna.get(0);
        highLight(articles);

        SearchResult searchMirna =  new SearchResult(num,ArticleUtils.ArticleListToDto(articles));
        return Result.success(searchMirna);
    }
}