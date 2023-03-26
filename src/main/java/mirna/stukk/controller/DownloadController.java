package mirna.stukk.controller;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.config.Result;
import mirna.stukk.service.ArticleService;
import mirna.stukk.utils.DoiUtils;
import mirna.stukk.utils.ExcelUtils;
import mirna.stukk.utils.PdfUtil;
import mirna.stukk.utils.StringToListUtils;
import org.apache.http.util.CharsetUtils;
import org.apache.lucene.analysis.CharacterUtils;
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
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

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
    
    @PostMapping("/GetArticleList")
    @ApiOperation("用Excel导出论文多篇论文的数据")
    public void downloadExcelArticle(@RequestBody List<ArticleDTO> articleDTOList, HttpServletResponse response) throws Exception {
        // 加载 Excel 模板文件
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
    
    
}

