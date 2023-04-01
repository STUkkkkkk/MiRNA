package mirna.stukk.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.Calculate;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.config.Result;
import mirna.stukk.service.ArticleService;
import mirna.stukk.service.CalculateService;
import mirna.stukk.service.PredictionService;
import mirna.stukk.service.RelationshipService;
import mirna.stukk.utils.*;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.velocity.VelocityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-23 9:25
 **/
@RestController
@RequestMapping("/download")
@Api(tags = "数据的导出下载")
public class DownloadController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private RelationshipService relationshipService;
    @Autowired
    private PredictionService predictionService;

    @Autowired
    private CalculateService calculateService;

    @PostMapping("/GetArticleList")
    @LimitAPI
    @ApiOperation("用Excel导出论文多篇论文的数据")
    public void downloadExcelArticle(@RequestBody List<ArticleDTO> articleDTOList, HttpServletResponse response) throws Exception {
        // 加载 Excel 模板文件
        if(articleDTOList == null){
            response.reset();
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(Result.error("555","数据为空或者异常，下载失败").toString());
            return ;
        }
        String templatePath = "templates/多篇论文数据模板.xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(templatePath);
//        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(templatePath);
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet;
        if(workbook instanceof SXSSFWorkbook){
            SXSSFWorkbook sxssfWorkbook = (SXSSFWorkbook) workbook;
            sheet = sxssfWorkbook.getXSSFWorkbook().getSheetAt(0);
        }
        else{
            sheet = workbook.getSheetAt(0);
        }

        // 更新 Excel 数据
        ExcelUtils.insertArticleList(sheet, articleDTOList);
        // 生成下载文件
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xlsx\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
    }
    
    @GetMapping("/GetOneArticle/{pmid}")
    @LimitAPI
    @ApiOperation("下载单篇论文的pdf")
    public void downLoadArticle(@PathVariable Long pmid , HttpServletResponse response) throws IOException {

        Article article = articleService.query().eq("pmid", pmid).one();
        if(article == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("555","查无此论文")));
            return ;
        }
        ArticleDTO articleDTO = ArticleDTO.builder()
                .pmid(article.getPmid())
                .title(article.getTitle())
                .authors(StringToListUtils.StringToList(article.getAuthors()))
                .types(StringToListUtils.StringToList(article.getType()))
                .keywords(StringToListUtils.StringToList(article.getKeywords()))
                .doi(DoiUtils.ToDoi(article.getDoi()))
                .url(DoiUtils.ToUrl(article.getDoi()))
                .library(article.getLibrary())
                .abs(article.getAbs())
                .date(article.getDate())
                .build();

//       设置response响应
        response.reset();
        response.setContentType("application/pdf");
        String fileName = pmid+".pdf";
        response.setHeader("Content-Disposition","attachment;filename=" + URLUtil.encode(fileName, CharsetUtil.CHARSET_UTF_8));
        VelocityContext context = new VelocityContext();
        context.put("title",articleDTO.getTitle());
        context.put("authors",articleDTO.getAuthors());
        context.put("types",article.getType());
        context.put("keywords",articleDTO.getKeywords());
        context.put("abstract",articleDTO.getAbs());
        context.put("pmid",pmid);
        context.put("date",articleDTO.getDate());
        try(ServletOutputStream outputStream = response.getOutputStream()){
            PdfUtil.pdfFile(context, "templates/demo.html", outputStream);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @GetMapping("/GetRelationShipByMiRNA")
    @LimitAPI
    @ApiOperation("通过mirna获取关系并下载")
    public void GetRelationShipByMiRNA(@RequestParam String mirnaName,HttpServletResponse response) throws IOException {

        List<RelationShip> relationShipList = relationshipService.query().eq("mirna_name", mirnaName).list();
        if(relationShipList == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("555","查无此数据")));
            return ;
        }
        String template = "templates/得证的关系模板.xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(template);
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet ;
        if(workbook instanceof SXSSFWorkbook){
            sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(0);
        }
        else{
            sheet = workbook.getSheetAt(0);
        }
        int hang = 2; //从第3行开始
        ExcelUtils.insertRelationshipList(sheet,relationShipList); //插入excel数据模板中

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xlsx\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();

    }


    @GetMapping("/GetRelationShipByDisease")
    @LimitAPI
    @ApiOperation("通过疾病获取关系并下载")
    public void GetRelationShipByDisease(@RequestParam String diseaseName,HttpServletResponse response) throws IOException {
        List<RelationShip> relationShipList = relationshipService.query().eq("disease_name", diseaseName).list();
        if(relationShipList == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("555","查无此数据")));
            return ;
        }
        String template = "templates/得证的关系模板.xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(template);
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet ;
        if(workbook instanceof SXSSFWorkbook){
            sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(0);
        }
        else{
            sheet = workbook.getSheetAt(0);
        }
        int hang = 2; //从第3行开始
        ExcelUtils.insertRelationshipList(sheet,relationShipList); //插入excel数据模板中

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xlsx\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
    }


    @GetMapping("/GetCalculateByMiRNA")
    @LimitAPI
    @ApiOperation("通过MiRNA获取预测关系并下载")
    public void GetCalculateByMiRNA(@RequestParam String mirnaName,HttpServletResponse response) throws IOException {
        List<Calculate> calculateList = calculateService.query().eq("mirna", mirnaName).list();
        if(calculateList == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("555","查无此数据")));
            return ;
        }
        String template = "templates/预测的关系模板.xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(template);
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet ;
        if(workbook instanceof SXSSFWorkbook){
            sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(0);
        }
        else{
            sheet = workbook.getSheetAt(0);
        }
        int hang = 2; //从第3行开始
        ExcelUtils.insertCalculateList(sheet,calculateList); //插入excel数据模板中
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xlsx\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
    }

    @GetMapping("/GetCalculateByDisease")
    @LimitAPI
    @ApiOperation("通过Disease获取预测关系并下载")
    public void GetCalculateByDisease(@RequestParam String diseaseName,HttpServletResponse response) throws IOException {
        List<Calculate> calculateList = calculateService.query().eq("disease", diseaseName).list();
        if(calculateList == null){
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSON.toJSONString(Result.error("555","查无此数据")));
            return ;
        }
        String template = "templates/预测的关系模板.xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(template);
        Workbook workbook = new SXSSFWorkbook(new XSSFWorkbook(inputStream));
        Sheet sheet ;
        if(workbook instanceof SXSSFWorkbook){
            sheet = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getSheetAt(0);
        }
        else{
            sheet = workbook.getSheetAt(0);
        }
        int hang = 2; //从第3行开始
        ExcelUtils.insertCalculateList(sheet,calculateList); //插入excel数据模板中
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.xlsx\"");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        workbook.close();
    }


}

