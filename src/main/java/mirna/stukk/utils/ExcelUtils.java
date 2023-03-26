package mirna.stukk.utils;

import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-23 10:19
 **/
public class ExcelUtils {

    /*
     * 1 2 4 7
     * @Param
     * @return 将论文数据放入excel
     * @author stukk
     * @create 2023-03-23
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
}
