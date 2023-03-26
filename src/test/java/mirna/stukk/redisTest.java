package mirna.stukk;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.Pojo.Relationship_disease;
import mirna.stukk.Pojo.Relationship_mirna;
import mirna.stukk.mapper.RelationshipMapper;
import mirna.stukk.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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
    public void testRedis(){
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
        redisTemplate.opsForHash().putAll("articles",result);
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

}
