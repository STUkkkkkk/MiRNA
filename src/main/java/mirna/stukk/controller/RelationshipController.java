package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Diagram;
import mirna.stukk.config.Result;
import mirna.stukk.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relation")
@Api(tags = "关系图获取接口")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    @GetMapping("/getMirna")
    @ApiOperation("根据mirna的名称获取对应的关系图")
    public Result<Diagram> GetMirnaDiagram(@RequestParam String mirnaName){
        //根据这个mirna的名字来找关系图
        return relationshipService.GetMirnaRelationship(mirnaName);
    }


    @GetMapping("/getDisease")
    @ApiOperation("根据Disease的名称获取对应的关系图")
    public Result<Diagram> GetDiseaseDiagram(@RequestParam String diseaseName){
        //根据这个mirna的名字来找关系图
        return relationshipService.GetDiseaseRelationship(diseaseName);
    }





}
