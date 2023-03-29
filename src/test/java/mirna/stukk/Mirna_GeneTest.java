package mirna.stukk;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mirna.stukk.Pojo.GeneMirnaRelationship;
import mirna.stukk.service.GeneMirnaRelationshipService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 21:31
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class Mirna_GeneTest {

    @Autowired
    private GeneMirnaRelationshipService geneMirnaRelationshipService;

    @Test
    public void test() throws IOException, CsvException, InterruptedException {
        String path = "D:\\大创项目\\数据\\TarBase8.0-human\\hsa\\hsa\\TarBase_Table";
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        int pi = 1;
        List<GeneMirnaRelationship> geneMirnaRelationshipList = new LinkedList<>();
        for(File file : files){
            if(file.isFile()){
                CSVReader csvReader = new CSVReader(new FileReader(path+"\\"+file.getName()));
                List<String[]> strings = csvReader.readAll();
                for(int i = 0;i<strings.size();i++){
                    if(i == 0){
                        continue; //第一行是标题数据，没屁用
                    }
                    String[] string = strings.get(i);
                    geneMirnaRelationshipList.add(GeneMirnaRelationship.builder().gene(string[0]).mirnaName(string[1]).publication(string[2])
                            .methods(string[3]).tissue(string[4].equals("N/A") ? null : string[4]).cellLine(string[5].equals("N/A") ? null:string[5]).build());
                }
            }
        }
        System.out.println(geneMirnaRelationshipList.size());
//        for(GeneMirnaRelationship geneMirnaRelationship : geneMirnaRelationshipList){
//            if((geneMirnaRelationship.getGene() != null && geneMirnaRelationship.getGene().length() > 31) ||
//                    geneMirnaRelationship.getMirnaName() != null && geneMirnaRelationship.getMirnaName().length() > 110 ||
//                    geneMirnaRelationship.getPublication() != null && geneMirnaRelationship.getPublication().length() >300 ||
//                    geneMirnaRelationship.getMethods() != null && geneMirnaRelationship.getMethods().length() > 110 ||
//                    geneMirnaRelationship.getTissue() != null && geneMirnaRelationship.getTissue().length() > 300 ||
//                    geneMirnaRelationship.getCellLine() != null && geneMirnaRelationship.getCellLine().length() > 300){
//                System.out.println(geneMirnaRelationship+"出问题了");
//            }
//        }
        int right = 330000;
        int left = 320000;
        while(left < geneMirnaRelationshipList.size()){

            if(right >= geneMirnaRelationshipList.size()){
                right = geneMirnaRelationshipList.size();
            }
            boolean b = geneMirnaRelationshipService.saveBatch(geneMirnaRelationshipList.subList(left, right));
            if(b){
                System.out.println("插入的数据量："+right);
            }
            else{
                System.out.println("插入失败啊/(ㄒoㄒ)/~~");
            }
            left = right;
            right += 10000;
            Thread.sleep(1000);

        }
//        boolean b = geneMirnaRelationshipService.saveBatch(geneMirnaRelationshipList);
//        if(b){
//            System.out.println("成功了！！！");
//        }
    }

}
