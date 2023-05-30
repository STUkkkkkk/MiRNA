package mirna.stukk;

import mirna.stukk.Pojo.MirnaAllMessage;
import mirna.stukk.Pojo.Mirna_3P_5P;
import mirna.stukk.Pojo.search.MirnaRelationSearch;
import mirna.stukk.service.MirnaAllMessageService;
import mirna.stukk.service.RelationshipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-27 20:59
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class MirnaAllMessageTest {

    @Autowired
    private MirnaAllMessageService mirnaAllMessageService;


    @Test
    public void test1(){
        MirnaAllMessage mirna_name = mirnaAllMessageService.query().eq("mirna_name", "hsa-let-7e").one();
        System.out.println(mirna_name);
    }

    @Test
    public void test2(){
        Mirna_3P_5P by3p5pName = mirnaAllMessageService.getBy3p5pName("hsa-let-7e-3p");
        System.out.println(by3p5pName);
    }

    @Resource
    private RelationshipService relationshipService;



}
