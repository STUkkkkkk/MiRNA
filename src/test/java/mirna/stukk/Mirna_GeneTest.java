package mirna.stukk;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import mirna.stukk.Pojo.GeneMirnaRelationship;
import mirna.stukk.mapper.MirnaMapper;
import mirna.stukk.service.GeneMirnaRelationshipService;
import mirna.stukk.service.MirnaService;
import mirna.stukk.utils.GiteeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

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
    @Value("${gitee.upload.path2D}")
    private String PATH2D;

    @Value("${gitee.upload.path3D}")
    private String PATH3D;


    @Autowired
    private MirnaMapper mirnaMapper;

    @Test
    public void Mirna2DStructTest() throws InterruptedException {
        String path = "D:\\miRTarDis项目\\改进项目\\2DStructure\\2DStructure"; //打开的文件夹路径
        File fileDir = new File(path);
        File[] files2D = fileDir.listFiles(); //获取所有文件
        path = "D:\\miRTarDis项目\\改进项目\\ArcSiagrams\\ArcSiagrams";
        fileDir = new File(path);
        File[] files3D = fileDir.listFiles();
        int length = files3D.length;
//        int length = 1;
        boolean ok = false;
        for(int i = 217;i<length;i++){

            File d2 = files2D[i];
            File d3 = files3D[i];
            if(d2.getName().equals("hsa-mir-4292.svg")){
                ok = true;
                continue;
            }
            if(!ok){
                continue;
            }
            try{

//                System.out.println("存入"+d2.getName());
                String sd2 = GiteeUtil.uploadFile(PATH2D,d2.getName(),fileToByte(d2));
                String sd3 = GiteeUtil.uploadFile(PATH3D,d3.getName(),fileToByte(d3));
                Map map2 = JSONUtil.toBean(sd2, Map.class);
                Map map3 = JSONUtil.toBean(sd3,Map.class);
                String content2 = map2.get("content").toString();
                String content3 = map3.get("content").toString();
                map2 = JSONUtil.toBean(content2,Map.class);
                map3 = JSONUtil.toBean(content3,Map.class);
                String url2 = map2.get("download_url").toString();
                String url3 = map3.get("download_url").toString();
                mirnaMapper.save2D3D(d2.getName().replace(".svg",""),d2.getName());
//                System.out.println("休息2秒");
                Thread.sleep(2000);
            }
            catch (Exception e){
                System.out.println(d2.getName()+"插不进去，跳过");
                continue;
            }
        }
    }

    public byte[] fileToByte(File file) {
        byte[] fileBytes = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            fileBytes = new byte[(int) file.length()];
            fis.read(fileBytes);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileBytes;
    }

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
            sleep(1000);

        }

    }

}
