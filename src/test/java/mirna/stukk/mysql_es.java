package mirna.stukk;


import com.alibaba.fastjson.JSON;
import mirna.stukk.Pojo.Article;
import mirna.stukk.mapper.ArticleMapper;
import mirna.stukk.service.ArticleService;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class mysql_es {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private ArticleService articleService;

    private Map<String,Object> month = new HashMap<>();

    {
        month.put("Jan","01");
        month.put("Feb","02");
        month.put("Mar","03");
        month.put("Apr","04");
        month.put("May","05");
        month.put("Jun","06");
        month.put("Jul","07");
        month.put("Aug","08");
        month.put("Sep","09");
        month.put("Oct","10");
        month.put("Nov","11");
        month.put("Dec","12");
    }

    @Test
    public void testDay() {
        System.out.println(       articleService.getByLimit(1,10));
    }

    @Test
    public void test() throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        DateTime now = DateTime.now();
        List<Article> articles = articleService.query().list();
        int pi = 0;
        for(Article article : articles){
            String date = article.getDate();
            String newDate = "";
            if(date != null && !date.equals("")){
                String[] dates = date.split(" ");
                if(dates.length == 3){
                    newDate = dates[0];
                    newDate += "-";
                    dates[2] = dates[2].length() > 1 ? dates[2]:"0"+dates[2];
                    newDate += (month.get(dates[1])==null? "01":month.get(dates[1]) +"-"+ dates[2]);
                }
                else if(dates.length == 2){
                    newDate = dates[0];
                    newDate += "-";
                    newDate += (month.get(dates[1])==null? "01":month.get(dates[1]) +"-"+ "01");
                }
                else if(dates.length == 1){
                    newDate = dates[0];
                    newDate += "-";
                    newDate += "01-01";

                }
                else{
                    newDate = null;
                }

                article.setDate(newDate);
            }
            String jsonString = JSON.toJSONString(article);
            IndexRequest indexRequest = new IndexRequest("article").source(jsonString,XContentType.JSON);
            bulkRequest.add(indexRequest);
            pi++;
            if(pi % 100 == 0){
                DateTime here = DateTime.now();
                System.out.println("经过了"+(here.getSecondOfDay() - now.getSecondOfDay())+"秒插入了"+pi+"条了");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                restHighLevelClient.bulk(bulkRequest,RequestOptions.DEFAULT);

                bulkRequest = new BulkRequest();
            }
        }

    }


    @Test
    public void testadddone() throws IOException{
        List<Article> articles = articleService.getByLimit(1,1000);
        System.out.println(articles);
    }


}

