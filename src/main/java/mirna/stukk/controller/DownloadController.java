package mirna.stukk.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.Calculate;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.DTO.MirnaRelationDTO;
import mirna.stukk.Pojo.DTO.PredictionDTO;
import mirna.stukk.Pojo.MirnaRelationDownload;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.PredictionMapper;
import mirna.stukk.mapper.RelationshipMapper;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private RelationshipMapper relationshipMapper;
    @Autowired
    private PredictionMapper predictionMapper;

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
        List<Long> pmids = articleDTOList.stream().map(ArticleDTO::getPmid).collect(Collectors.toList()); //获取干净的论文数据
        List<Article> articleList = articleService.getByPmids(pmids);
        articleDTOList = ArticleUtils.ArticleListToDto(articleList);
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

    @PostMapping("/getMirnaRelationship")
    @LimitAPI
    @ApiOperation("通过Disease获取预测关系并下载")
    public void getMirnaRelationshipData(@RequestBody MirnaRelationDownload mirnaRelationDownload, HttpServletResponse response ) throws IOException {
        List<String> mirnas = mirnaRelationDownload.getMirnas();
        List<String> diseases = mirnaRelationDownload.getDiseases();
        Integer downloadType = mirnaRelationDownload.getDownloadType();
        Double maxRelevance = mirnaRelationDownload.getMaxRelevance();
        Integer resource = mirnaRelationDownload.getResource();
        Double minRelevance = mirnaRelationDownload.getMinRelevance();
        Integer row = mirnaRelationDownload.getRow();
        if(minRelevance > maxRelevance){
            throw new BaseException("5001","查询参数错误,选择的最小关联度大于最大关联度");
        }
        if(mirnas != null && mirnas.size() > 10 || diseases != null &&  diseases.size() > 10){
            throw new BaseException("5001","查询mirna、disease数量过多，请重试");
        }
        List<MirnaRelationDTO> mirnaRelationDTOList = new LinkedList<>();
        int total = 0;
        int num1 = 0;
        int num2 = row;
        if(resource == 1){
            //已证实
            List<Object> objects = relationshipMapper.getByMessage(mirnas, diseases,num1, num2);
            if(objects == null){
                Result.success();
            }
            total = ((List<Integer>)objects.get(1)).get(0);
            List<RelationShip> relationShipList = ((List<RelationShip>)objects.get(0));
            for(RelationShip relationShip : relationShipList){
                MirnaRelationDTO mirnaRelationDTO = MirnaRelationDTO.builder().pmid(null).mirnaName(relationShip.getMirnaName())
                        .disease(relationShip.getDiseaseName()).relevance(1.0).resource(CommonUtil.HMDD).pmid(relationShip.getPmid()).build();
                mirnaRelationDTOList.add(mirnaRelationDTO);
            }
        }
        else{
//            未证实,预测的
            List<Object> objects = predictionMapper.getPredictionDTO(mirnas, diseases,minRelevance,maxRelevance,num1, num2);
            if(objects == null){
                throw new BaseException("50010","下载失败");
            }
            total = ((List<Integer>) objects.get(1)).get(0);
            List<PredictionDTO> predictionDTOList = (List<PredictionDTO>) objects.get(0);
            for(PredictionDTO predictionDTO : predictionDTOList){
                MirnaRelationDTO mirnaRelationDTO = MirnaRelationDTO.builder().pmid(null).mirnaName(predictionDTO.getMirna())
                        .disease(predictionDTO.getDisease()).resource(CommonUtil.PM).relevance(predictionDTO.getForecastRelevance()).build();
                mirnaRelationDTOList.add(mirnaRelationDTO);
            }
        }
        if(downloadType == 0){
            String template = "templates/Mirna-Disease-Pmid数据模板.xlsx";
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
            ExcelUtils.insertMirnaRelationship(sheet,mirnaRelationDTOList); //插入excel数据模板中
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
        else if(downloadType == 1){
//            下载csv文件：
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=download.csv");
//            还没好
        }
    }






}

