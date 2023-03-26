package mirna.stukk.controller;

import com.baomidou.mybatisplus.extension.api.R;
import io.swagger.annotations.*;
import mirna.stukk.Pojo.Disease;
import mirna.stukk.Pojo.MiRNA;
import mirna.stukk.Pojo.Prediction;
import mirna.stukk.config.Result;
import mirna.stukk.service.DiseaseService;
import mirna.stukk.service.MirnaService;
import mirna.stukk.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prediction")
@Api(tags = "预测接口")
@ApiImplicitParams(
        @ApiImplicitParam(name = "diseaseName", value = "疾病名称")
)
public class PredictionController {


    @Autowired
    private PredictionService predictionService;

    @Autowired
    private DiseaseService diseaseService;

    @Autowired
    private MirnaService mirnaService;



    @GetMapping("/getByDisease")
    @ApiOperation("根据疾病名称找到预测的mirna和对应的准确度")
    public Result<List<Prediction>> getByDisease(@RequestParam String diseaseName){

        Disease disease = diseaseService.query().eq("disease_name", diseaseName).one();
        if(disease == null){
            return Result.error("555","不存在这个疾病名称");
        }
        List<Prediction> disease_id = predictionService.query().eq("disease_id", disease.getId()).list();
        return Result.success(disease_id);
    }

    @GetMapping("/getByMirna")
    @ApiOperation("根据mirna名称找到预测的疾病和对应的准确度")
    public Result<List<Prediction>> getByMirna(@RequestParam String mirnaName){

        MiRNA miRNA = mirnaService.query().eq("mirna_name", mirnaName).one();
        if(miRNA == null){
            return Result.error("555","不存在这个MiRNA名称");
        }
        List<Prediction> predictionList = predictionService.query().eq("mirna_id", miRNA.getId()).list();
        return Result.success(predictionList);
    }
}
