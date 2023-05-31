package mirna.stukk.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.Calculate;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.DTO.MirnaRelationDTO;
import mirna.stukk.Pojo.DTO.PredictionDTO;
import mirna.stukk.Pojo.MirnaRelationDownload;
import mirna.stukk.Pojo.RelationShip;
import mirna.stukk.Pojo.search.MirnaRelationshipData;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
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

    @GetMapping(value = "/testDownload",produces = "text/csv")
    @ApiOperation("测试使用，下载csv")
    public void test(HttpServletResponse response) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

        response.setContentType("application/ms-txt.numberformat:@");
        response.setHeader("Pragma","public");

        List<MirnaRelationDTO> mirnaRelationDTOList = new LinkedList<>();
        mirnaRelationDTOList.add(MirnaRelationDTO.builder().mirnaName("mirna").disease("disease").build());

        ColumnPositionMappingStrategy mapStrategy = new ColumnPositionMappingStrategy();

        mapStrategy.setType(MirnaRelationDTO.class);

        //和字段名对应
        String[] columns = new String[]{"miRNA","Disease","Resource","Pmid","Relevance"};
        mapStrategy.setColumnMapping(columns);

        StatefulBeanToCsv btcsv = new StatefulBeanToCsvBuilder(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withMappingStrategy(mapStrategy)
                .withSeparator(',')
                .build();

        btcsv.write(mirnaRelationDTOList);
    }

    @GetMapping(value = "/export", produces = "text/csv")
    @ApiOperation("测试使用。。。")
    public ResponseEntity<String> exportData() throws Exception {
        // 假设从数据库中获取的数据如下
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"John", "Smith", "35"});
        data.add(new String[]{"Jane", "Doe", "25"});
        data.add(new String[]{"Bob", "Johnson", "45"});
        // 生成CSV文件内容
        StringWriter writer = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(writer);
        csvWriter.writeAll(data);
        csvWriter.close();
        // 设置响应头部信息
        HttpHeaders headers = new HttpHeaders();
        String filename = "download.csv";
        headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        // 返回包含CSV数据的响应实体
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(writer.toString());
    }

    @GetMapping("/exportTxt")
    @ApiOperation("测试下载txt文件")
    public void export(HttpServletResponse response) throws IOException {
        List<MirnaRelationDTO> mirnaRelationDTOList = new LinkedList<>();
        mirnaRelationDTOList.add(MirnaRelationDTO.builder().mirnaName("mirna").disease("disease").pmid(null).resource("0").relevance(0.11).build());

        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"miRNA","Disease","Resource","Pmid","Relevance"});
        for(MirnaRelationDTO mirnaRelationDTO : mirnaRelationDTOList){
            String str[] = new String[5];
            str[0] = mirnaRelationDTO.getMirnaName();
            str[1] = mirnaRelationDTO.getDisease();
            str[2] = mirnaRelationDTO.getResource();
            str[3] = mirnaRelationDTO.getPmid() == null ? "NAN":mirnaRelationDTO.getPmid().toString();
            str[4] = mirnaRelationDTO.getRelevance() == null ? "NAN":mirnaRelationDTO.getRelevance().toString();
            data.add(str);
        }
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=download.txt");
        OutputStream os = response.getOutputStream();
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
        for(String[] arr : data){
            String line = "";
            for(int i = 0;i < arr.length; i++){
                line += arr[i];
                if(i < arr.length - 1){
                    line += "\t";
                }
            }
            bw.write(line + "\n");
        }
        bw.flush();
        bw.close();
    }



    @GetMapping("/exportJson")
    @ApiOperation("测试下载json文件")
    public void exportJSON(HttpServletResponse response) throws IOException {
        List<MirnaRelationDTO> mirnaRelationDTOList = new LinkedList<>();
        mirnaRelationDTOList.add(MirnaRelationDTO.builder().mirnaName("mirna").disease("disease").pmid(null).resource("0").relevance(0.11).build());
        String json = JSONUtil.toJsonStr(mirnaRelationDTOList);
        response.setContentType("application/json");
        response.setHeader("Content-Disposition", "attachment; filename=data.json");
        response.getWriter().write(json);
    }



    @PostMapping("/getMirnaRelationship")
    @LimitAPI
    @ApiOperation("获取mirna-disease-pmid-来源数据并下载excel数据")
    public ResponseEntity<String>  getMirnaRelationshipData(@RequestBody MirnaRelationDownload mirnaRelationDownload, HttpServletResponse response ) throws IOException {
        List<String> mirnas = mirnaRelationDownload.getMirnas();
        List<String> diseases = mirnaRelationDownload.getDiseases();
        Double maxRelevance = mirnaRelationDownload.getMaxRelevance();
        Integer resource = mirnaRelationDownload.getResource();
        Double minRelevance = mirnaRelationDownload.getMinRelevance();
        Integer row = mirnaRelationDownload.getRow();
        Integer downloadType = mirnaRelationDownload.getDownloadType();
        if(minRelevance > maxRelevance){
            throw new BaseException("5001","查询参数错误,选择的最小关联度大于最大关联度");
        }
        if(mirnas != null && mirnas.size() > 10 || diseases != null &&  diseases.size() > 10){
            throw new BaseException("5001","查询mirna、disease数量过多，请重试");
        }
        MirnaRelationshipData mirnaRelationshipData = relationshipService.getMirnaRelationshipDataWay(mirnas,diseases,minRelevance,maxRelevance,1,row,resource);
        List<MirnaRelationDTO> mirnaRelationDTOList = mirnaRelationshipData.getMirnaRelationDTOList();
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
//            下载csv
            // 假设从数据库中获取的数据如下
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"miRNA","Disease","Resource","Pmid","Relevance"});
            for(MirnaRelationDTO mirnaRelationDTO : mirnaRelationDTOList){
                String str[] = new String[5];
                str[0] = mirnaRelationDTO.getMirnaName();
                str[1] = mirnaRelationDTO.getDisease();
                str[2] = mirnaRelationDTO.getResource();
                str[3] = mirnaRelationDTO.getPmid() == null ? "":mirnaRelationDTO.getPmid().toString();
                str[4] = mirnaRelationDTO.getRelevance() == null ? "":mirnaRelationDTO.getRelevance().toString();
                data.add(str);
            }
            // 生成CSV文件内容
            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer);
            csvWriter.writeAll(data);
            csvWriter.close();
            // 设置响应头部信息
            HttpHeaders headers = new HttpHeaders();
            String filename = "download.csv";
            headers.add("Content-Disposition", "attachment; filename=\"" + filename + "\"");
            // 返回包含CSV数据的响应实体
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(writer.toString());
        }
        else if(downloadType == 2){
            //下载txt文件
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"miRNA","Disease","Resource","Pmid","Relevance"});
            for(MirnaRelationDTO mirnaRelationDTO : mirnaRelationDTOList){
                String str[] = new String[5];
                str[0] = mirnaRelationDTO.getMirnaName();
                str[1] = mirnaRelationDTO.getDisease();
                str[2] = mirnaRelationDTO.getResource();
                str[3] = mirnaRelationDTO.getPmid() == null ? "NAN":mirnaRelationDTO.getPmid().toString();
                str[4] = mirnaRelationDTO.getRelevance() == null ? "NAN":mirnaRelationDTO.getRelevance().toString();
                data.add(str);
            }
            response.setContentType("text/plain");
            response.setHeader("Content-Disposition", "attachment;filename=download.txt");
            OutputStream os = response.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            for(String[] arr : data){
                String line = "";
                for(int i = 0;i < arr.length; i++){
                    line += arr[i];
                    if(i < arr.length - 1){
                        line += "\t";
                    }
                }
                bw.write(line + "\n");
            }
            bw.flush();
            bw.close();
        }
        else {
//           下载json文件
            String json = JSONUtil.toJsonStr(mirnaRelationDTOList);
            response.setContentType("application/json");
            response.setHeader("Content-Disposition", "attachment; filename=data.json");
            response.getWriter().write(json);
        }
        return null;
    }






}

