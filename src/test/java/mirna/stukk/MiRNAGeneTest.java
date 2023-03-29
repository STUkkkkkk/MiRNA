package mirna.stukk;

import mirna.stukk.Pojo.MirnaStruct;
import mirna.stukk.service.MirnaStructService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 20:21
 **/
@SpringBootTest
@RunWith(SpringRunner.class)
public class MiRNAGeneTest {

    @Autowired
    private MirnaStructService mirnaStructService;

    @Test
    public void mirnaTest() throws IOException {
        String path = "D:\\大创项目\\数据\\mirna的结构图.txt";
        BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
        String p;
        int cnt = 1;
        List<MirnaStruct> mirnaStructList = new LinkedList<>();
        HashMap<String, Boolean> map = new HashMap<>();
        List<String> mirnaNameList = new LinkedList<>();
        String first = null,second = null,third = null,fourth = null,fifth = null;
        while((p = bf.readLine()) != null && cnt <= 308712){
            if(cnt % 8 == 1){
                mirnaNameList = new LinkedList<>();
                String mirna[] = p.split(" ");
                for(int i = 0;i<mirna.length;i++){
                    if(i == 0){
                        String mid_mirna = mirna[i].substring(1);
                        if(!map.containsKey(mid_mirna.toLowerCase())){
                            map.put(mid_mirna.toLowerCase(),true);
                            mirnaNameList.add(mid_mirna);
                        }
                    }
                    else{
                        if(mirna[i].contains("[") && mirna[i].contains("]")){
                            String mid_mirna = mirna[i].substring(1,mirna[i].length()-1).split(":")[0];
                            if(!map.containsKey(mid_mirna.toLowerCase())){
                                map.put(mid_mirna.toLowerCase(),true);
                                mirnaNameList.add(mid_mirna);
                            }
                        }
                    }
                }
            }
            else if(cnt % 8 == 3){
                first = p;
            }
            else if(cnt % 8 == 4){
                second = p;
            }
            else if(cnt % 8 == 5){
                third = p;
            }
            else if(cnt % 8 == 6){
                fourth = p;
            }
            else if(cnt % 8 == 7){
                fifth = p;
            }
            else if(cnt % 8 == 0){
                System.out.println("正在插入数据.....");
                for(String mirna : mirnaNameList){
                    mirnaStructList.add(MirnaStruct.builder().mirnaName(mirna).first(first).second(second).third(third).fourth(fourth).fifth(fifth).build());
                }
            }
            cnt++;
        }
//        for(MirnaStruct mirnaStruct : mirnaStructList){
//            if(mirnaStruct.getFifth().length() > 500){
//                System.out.println(mirnaStruct.getFifth());
//            }
//        }
        boolean b = mirnaStructService.saveBatch(mirnaStructList);
        if(b){
            System.out.println("插入成功啦");
        }
        else{
            System.out.println("寄了，插入失败");
        }
    }

    @Test
    public void test(){
        MirnaStruct mirnaStruct = mirnaStructService.getById(10);
        System.out.println(mirnaStruct.getFifth());
        System.out.println(mirnaStruct.getSecond());
        System.out.println(mirnaStruct.getThird());
        System.out.println(mirnaStruct.getFourth());
        System.out.println(mirnaStruct.getFifth());
    }

}
