package mirna.stukk;

import mirna.stukk.Pojo.Article;
import mirna.stukk.mapper.PredictionMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Random;

@SpringBootTest
@RunWith(SpringRunner.class)
class StukkApplicationTests {


//    @Test
//    void contextLoads() {
//        List<Object> objects = searchMirnaMapper.GetByMirna(1, "hsa-mir-29a");
//        Integer num = ((List<Integer>)objects.get(1)).get(0);
//        List<Article> articles = (List<Article>) objects.get(0);
//        System.out.println(articles);
//        System.out.pri
//        ntln(num);
//    }




    @Autowired
    private PredictionMapper predictionMapper;

    @Test
    public void PrectionTest(){
        String mirnaName = "hsa-let-7d";
        long l = System.currentTimeMillis();
        predictionMapper.getDiseaseListPredictionByMirna(mirnaName,null,new Random().nextInt(300));
        long l1 = System.currentTimeMillis();
        System.out.println(l1-l +"ms");
    }

}

