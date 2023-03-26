package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Disease;
import mirna.stukk.Pojo.MiRNA;
import mirna.stukk.config.Result;
import mirna.stukk.service.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disease")
@Api(tags = "疾病接口")
public class DiseaseController {

    @Autowired
    DiseaseService diseaseService;

    @GetMapping
    @ApiOperation("模糊查询疾病名称")
    public Result GetLikeName(@RequestParam String diseaseName){
        List<Disease> diseases = diseaseService.query().like("disease_name", diseaseName).list();
        return Result.success(diseases);
    }



    @GetMapping("/all")
    @ApiOperation("获取所有的疾病")
    public Result<List<Disease>> getAll(){
        List<Disease> list = diseaseService.query().list();
        return Result.success(list);
    }


    @GetMapping("/getById/{id}")
    @ApiOperation("根据id获取疾病")
    public Result<Disease> getById(@PathVariable Long id){
        Disease disease = diseaseService.getById(id);
        return Result.success(disease);
    }

    @GetMapping("/getByName")
    @ApiOperation("根据名字获取疾病")
    public Result<Disease> getById(@RequestParam String diseaseName){
        Disease disease = diseaseService.query().eq("disease_name",diseaseName).one();
        return Result.success(disease);
    }


}
