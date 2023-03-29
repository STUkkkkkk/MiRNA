package mirna.stukk;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
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
    public void test() throws IOException, CsvException {
        String path = "D:\\大创项目\\数据\\TarBase8.0-human\\hsa\\hsa\\TarBase_Table";
        File fileDir = new File(path);
        File[] files = fileDir.listFiles();
        int pi = 1;
        for(File file : files){
            if(pi == 1){
                pi++;
                if(file.isFile()){
                    CSVReader csvReader = new CSVReader(new FileReader(path+"\\"+file.getName()));
                    List<String[]> strings = csvReader.readAll();
                    for(String[] string : strings){
                        System.out.println(string.length);
                        for(String s : string){
                            System.out.print(s+"===>");
                        }
                        System.out.println();
                    }
                }
            }
        }
    }

}
