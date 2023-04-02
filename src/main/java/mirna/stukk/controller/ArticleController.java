package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.Pojo.SearchResult;
import mirna.stukk.config.Result;
import mirna.stukk.service.ArticleService;
import mirna.stukk.utils.LimitAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@Api(tags = "文章接口")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/like")
    @LimitAPI(limit = 2,second = 10)
    @ApiOperation("类似百度检索全部论文，根据message 来检索所有有关message的论文")
    public Result<SearchResult> getByLike(@RequestParam String message, @RequestParam Integer pageNum, @RequestParam Integer pageSize) throws Exception {
        return articleService.getByLike(message,pageNum,pageSize);
    }

    @GetMapping("/getByPmid/{pmid}")
    @ApiOperation("根据pmid获取单篇论文的信息")
    public Result<ArticleDTO> getByPmid(@PathVariable Long pmid)  throws IOException {
        return articleService.getByPmid(pmid);
    }



}
