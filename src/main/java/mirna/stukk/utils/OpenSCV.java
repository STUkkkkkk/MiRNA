package mirna.stukk.utils;

import mirna.stukk.Pojo.Relationship_disease;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class OpenSCV {

    public static void main(String[] args) throws IOException {
//        BufferedReader bf = Files.newBufferedReader(Paths.get("D:\\大创项目\\数据\\疾病-论文高亮\\title_disease.csv"));
//        String p ;
//        List<Relationship_disease> relationshipDiseaseList = new LinkedList<>();
//        int pi = 0;
//        while((p = bf.readLine()) != null){
//            String colums[] = p.split(",");
//            String[] split = colums[1].split("'");
//            for(String k : split){
//                if(k==null || k.equals("[") || k.equals("]") || k.equals(" ") ){
//                    continue;
//                }
//                Relationship_disease relationship_disease = new Relationship_disease();
//                relationship_disease.setPmid(Long.parseLong(colums[0]));
//                relationship_disease.setDiseaseName(k);
//                relationshipDiseaseList.add(relationship_disease);
//            }
//        }

        Long a = 11L;
        Long b = 11L;
        System.out.println(a == b);

//        System.out.println(relationshipDiseaseList);
    }
}
