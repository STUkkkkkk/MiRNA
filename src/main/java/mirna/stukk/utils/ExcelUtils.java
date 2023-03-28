package mirna.stukk.utils;

import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.Calculate;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.Prediction;
import mirna.stukk.Pojo.RelationShip;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-23 10:19
 **/
public class ExcelUtils {

    public static void main(String[] args) throws IOException {
        String path = "D:\\大创项目\\数据\\prediction.xlsx";
        FileInputStream inputStream = new FileInputStream(new File(path));
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet ;
        if(workbook instanceof SXSSFWorkbook){
            sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(1);
        }
        else{
            sheet = workbook.getSheetAt(1);
        }
        int n = 189585;
        List<Calculate> calculateList = new LinkedList<>();
        for(int i = 1;i<=n;i++){
            Calculate calculate = new Calculate();
            Row row = sheet.getRow(i);
            int hang = 0;
            Cell cell = row.getCell(hang++);
            String mirna = cell.getStringCellValue();
            cell = row.getCell(hang++);
            String disease = cell.getStringCellValue();
            cell = row.getCell(hang++);
            Integer proved = (int) Double.parseDouble(String.valueOf(cell.getNumericCellValue()));
            cell = row.getCell(hang);
            Double forcast = Double.parseDouble(String.valueOf(cell.getNumericCellValue()));

            String diseaseList[] = disease.split(", ");
            System.out.println("你妈的真出逗号了！！！");
            disease = "";
            for(int k = 0;k<diseaseList.length;k++){
                disease = disease + diseaseList[k];
                if(k != diseaseList.length - 1){
                    disease += " ";
                }
            }
            calculateList.add(Calculate.builder().mirna(mirna).disease(disease).proved(proved).forecastRelevance(forcast).build());
        }

    }

    /*
     * 1 2 4 7
     * @Param
     * @return 将论文数据放入excel
     * @author stukk
     * @create 2023-03-23
     **/

    /*
     *
     * @Param 写入excel表格
     * @return void
     * @author stukk
     * @create 2023-03-28
     **/
    public static void insertCell(Row row ,int pi, String data){
        Cell cell = row.getCell(pi);
        if(cell == null){
            cell = row.createCell(pi);
        }
        cell.setCellValue(data);
    }

    public static void insertArticle(Row row,ArticleDTO article){
        int lie = 0;
        insertCell(row, lie++ ,article.getPmid() == null ? "":article.getPmid().toString());
        insertCell(row, lie++ , article.getTitle());
        insertCell(row, lie++ ,article.getTypes() == null ? "":article.getTypes().toString());
        insertCell(row, lie++ ,article.getAuthors() == null ? "":article.getAuthors().toString());
        insertCell(row, lie++ , article.getDoi());
        insertCell(row, lie++ ,article.getKeywords() == null ? "":article.getKeywords().toString());
        insertCell(row, lie++ , article.getLibrary());
        insertCell(row, lie++ ,article.getAbs());
        insertCell(row, lie ,article.getDate());
    }

    public static void insertArticleList(Sheet sheet , List<ArticleDTO> articles){
        int pi = 2;
        for(ArticleDTO article : articles){
            Row row = sheet.getRow(pi);//获取第pi行
            if(row == null){
                row = sheet.createRow(pi);
            }
            insertArticle(row,article);
            pi++;
        }
    }


    public static void insertRelationship(Row row,RelationShip relationShip){
        int lie = 0;
        insertCell(row, lie++ ,relationShip.getPmid() == null ? "":relationShip.getPmid().toString());
        insertCell(row, lie++ , relationShip.getMirnaName());
        insertCell(row,lie,relationShip.getDiseaseName());
    }

    public static void insertRelationshipList(Sheet sheet , List<RelationShip> relationShipList){
        int pi = 2;
        for(RelationShip relationShip : relationShipList){
            Row row = sheet.getRow(pi);//获取第pi行
            if(row == null){
                row = sheet.createRow(pi);
            }
            insertRelationship(row, relationShip);
            pi++;
        }
    }

    public static void insertCalculateList(Sheet sheet, List<Calculate> calculateList) {
        int pi = 2;
        for(Calculate calculate : calculateList){
            Row row = sheet.getRow(pi);
            if(row == null){
                row = sheet.createRow(pi);
            }
            insertCalculate(row, calculate);
            pi++;
        }
    }

    private static void insertCalculate(Row row, Calculate calculate) {
        int lie = 0;
        insertCell(row , lie++ , calculate.getMirna());
        insertCell(row, lie++ , calculate.getDisease());
        insertCell(row,lie++,calculate.getProved().toString());
        insertCell(row , lie ,calculate.getForecastRelevance().toString());
    }
}
