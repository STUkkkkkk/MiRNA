package mirna.stukk;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.Pojo.Relationship_disease;
import mirna.stukk.Pojo.Relationship_mirna;
import mirna.stukk.mapper.RelationshipMapper;
import mirna.stukk.service.ArticleService;
import mirna.stukk.utils.PrefixUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class redisTest {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RelationshipMapper relationshipMapper;



    @Test
    public void newRedisTest(){
        redisTemplate.opsForValue().set("1111","11111");
        System.out.println("插入成功");
    }

    @Test
    public void ArticletestRedis(){ //redispull进入 artice
//        redisTemplate.opsForHash().put("articles","1001","disease,fuck");
//        redisTemplate.opsForHash().put("articles","1003","stukk");
//        List<String> list = new LinkedList<>();
//        list.add("1001");
//        list.add("1003");
//        Object articles = redisTemplate.opsForHash().multiGet("articles", list);
//        System.out.println(articles);
        List<Relationship_mirna> relationshipMirnaList = relationshipMapper.getRelationshipMirnaList();
        List<Relationship_disease> relationshipDiseaseList = relationshipMapper.getRelationshipDiseaseList();
        for(Relationship_disease relationship_disease : relationshipDiseaseList){
            Relationship_mirna relationship_mirna = new Relationship_mirna();
            relationship_mirna.setPmid(relationship_disease.getPmid());
            relationship_mirna.setMirnaName(relationship_disease.getDiseaseName());
            relationshipMirnaList.add(relationship_mirna);
        }
        relationshipMirnaList.sort((relationship_mirna, t1) -> Math.toIntExact(relationship_mirna.getPmid() - t1.getPmid()));
//        System.out.println(relationshipMirnaList);

        Long pmid = -1L;
        List<String> list = null;
        Map<Long,String> result = new HashMap<>();
        for(int i = 0;i<relationshipMirnaList.size();i++){
            Relationship_mirna relationship_mirna = relationshipMirnaList.get(i);
            if(!pmid.equals(relationship_mirna.getPmid()) ){
                if(pmid != -1L){
                    result.put(pmid , JSON.toJSONString(list));
                }
                pmid = relationship_mirna.getPmid();
                list = new LinkedList<>();
                list.add(relationship_mirna.getMirnaName());
            }
            else{
                if(!list.contains(relationship_mirna.getMirnaName())){
                    list.add(relationship_mirna.getMirnaName());
                }
            }
        }
//        redisTemplate.opsForHash().putAll("articles",result);
    }

    @Test
    public void testExcel() throws IOException {
        BufferedReader bf = Files.newBufferedReader(Paths.get("D:\\大创项目\\数据\\疾病-论文高亮\\abstract_disease.csv"));
        String p ;
        List<Relationship_disease> relationshipDiseaseList = new LinkedList<>();
        while((p = bf.readLine()) != null){
            String colums[] = p.split(",");
            String[] split = colums[1].split("'");
            for(String k : split){
                if(k==null || k.equals("[") || k.equals("]") || k.equals(" ") ){
                    continue;
                }
                Relationship_disease relationship_disease = new Relationship_disease();
                relationship_disease.setPmid(Long.parseLong(colums[0]));
                relationship_disease.setDiseaseName(k);
                relationshipDiseaseList.add(relationship_disease);
            }
        }
        int i = relationshipMapper.AddRelationshipDisease(relationshipDiseaseList);
        if(i > 0){
            log.info("成功插入");
        }
    }


    @Test
    public void testSearchFromRedis(){
//        Object articles = redisTemplate.opsForHash().get("articles", 16712479L);
//        System.out.println(articles);
//        redisTemplate.opsForValue().set("kkkk","stukk");
//        Object kkkk = redisTemplate.opsForValue().get("kkkk");
//        System.out.println(kkkk);
        List<Relationship_disease> relationshipDiseaseList = new LinkedList<>();
        Relationship_disease relationship_disease = new Relationship_disease(111L,"例子");
        relationshipDiseaseList.add(relationship_disease);
        int i = relationshipMapper.AddRelationshipDisease(relationshipDiseaseList);
        if(i > 0){
            System.out.println("插入成功");
        }


    }

    @Test
    public void test(){
        List<Long> list = new LinkedList<>();
        list.add(23990444L);
        list.add(21880981L);
        List articles = redisTemplate.opsForHash().multiGet("articles", list);
        System.out.println(articles);
    }

    @Test
    public void RecordTest(){
        int n = 5;
        LocalDateTime localDateTime = LocalDateTime.now(); //获取现在的时间
        String date = localDateTime.format(DateTimeFormatter.ISO_DATE);
//        LocalDateTime localDateTime1 = localDateTime.minusDays(1);
//        String date = localDateTime1.format(DateTimeFormatter.ISO_DATE);
        String key = PrefixUtils.MiRNARecordKey + date; //设置格式


//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_1",89);
//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_2",15);
//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_3",102);
//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_4",1022);
        redisTemplate.opsForZSet().incrementScore(key,"hsa-let-7a-5p",1020);
//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_6",190);
        redisTemplate.opsForZSet().incrementScore(key,"hsa-let-7f-2-7p",11);
//        redisTemplate.opsForZSet().incrementScore(key,"hsa_ok_8",2);
        redisTemplate.opsForZSet().incrementScore(key,"hsa-mir-4527",100);

        redisTemplate.opsForZSet().incrementScore(key,"hsa-mir-467",1);

        System.out.println("ok");

//        redisTemplate.opsForZSet().union()
//        Date date1 = DateUtil.date();
////获取本周的第一天
//        DateTime beginOfWeek = DateUtil.beginOfWeek(date1);
////到今天一共有几天
//        long diffDay = DateUtil.between(date1, beginOfWeek, DateUnit.DAY) + 1;
//
//        List<String> keys = new ArrayList<>();
//        for(int i = 0; i < diffDay; i++) {
//            //把需要查询的天数放一起
//            keys.add(PrefixUtils.MiRNARecordKey + DateUtil.formatDate(DateUtil.offsetDay(beginOfWeek, i)));
//        }
//
////redis使用unionAndStore做合并，将结果集放在另一个的key，也就是第三个参数
////        redisTemplate.opsForZSet().unionAndStore("kk",keys, "RankWeek");
//
////查询结果集用employeeRankWeek这个key
//        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores("RankWeek", 0, n-1);
//        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
//        for(int i = 0, size = jsonArray.size(); i < size; i++) {
//            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
//            System.out.println("搜索的词汇：" + o.getString("value") + ", 搜索次数：" + o.getLongValue("score"));
//        }

//        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n-1); //查找前n个
//        System.out.println(set);
//        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
//        System.out.println(date +"的记录:");
//        for(int i = 0, size = jsonArray.size(); i < size; i++) {
//            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
//            System.out.println("搜索的词汇：" + o.getString("value") + ", 搜索次数：" + o.getLongValue("score"));
//        }
    }

    @Test
    public void testAdd(){
        Date date = new Date();
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        long dayLength = DateUtil.between(date, beginOfWeek, DateUnit.DAY) + 1; //获取这周多少天
//        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            String key = PrefixUtils.ArticleDiseaseRecordKey + DateUtil.formatDate(DateUtil.offsetDay(beginOfWeek, i));
            redisTemplate.opsForZSet().add(key,"Leukemia",111);
            redisTemplate.opsForZSet().add(key,"Colon Neoplasms",134);
            redisTemplate.opsForZSet().add(key,"Lung Neoplasms",1);
            redisTemplate.opsForZSet().add(key,"Lupus Vulgaris",31);
            redisTemplate.opsForZSet().add(key,"Atherosclerosis",313);
            redisTemplate.opsForZSet().add(key,"Melanoma",23);
            redisTemplate.opsForZSet().add(key,"Heart Failure",10);
            System.out.println("成功插入："+key);
        }
    }

}
