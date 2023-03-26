package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.SearchResult;
import mirna.stukk.config.Result;
import mirna.stukk.service.SearchResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@Api(tags = "精确查询获取论文接口")
public class SearchResultController {

    @Autowired
    SearchResultService searchResultService;

    @GetMapping("/ByDiseaseName")
    @ApiOperation("根据疾病名称获取论文")
    public Result<SearchResult> GetByDisease(@RequestParam Integer pageNum, @RequestParam String diseaseName , @RequestParam String startTime, @RequestParam String endTime, @RequestParam Integer pageSize){
        return searchResultService.getByDisease(pageNum,diseaseName,startTime,endTime,pageSize);
    }

    @GetMapping("/ByMirnaName")
    @ApiOperation("根据mirna名称获取论文")
    public Result<SearchResult> GetByMirna(@RequestParam Integer pageNum, @RequestParam String mirnaName , @RequestParam String startTime, @RequestParam String endTime,@RequestParam Integer pageSize){
        return searchResultService.getByMirna(pageNum,mirnaName,startTime,endTime,pageSize);
    }

}
