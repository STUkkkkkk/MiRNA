package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.GeneMirnaRelationship;
import mirna.stukk.config.Result;
import mirna.stukk.service.GeneMirnaRelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-30 0:16
 **/
@RestController
@RequestMapping("/GeneMirnaRelationship")
@Api(tags = "基因和MiRNA的关系接口")
public class GeneMirnaRelationshipController {

    @Autowired
    private GeneMirnaRelationshipService geneMirnaRelationshipService;

    @GetMapping("/GetByMirnaName")
    @ApiOperation("通过mirna名字来获取对应的基因关系")
    public Result<List<GeneMirnaRelationship>> GetByMirnaName(@RequestParam String mirnaName){
        if(mirnaName == null){
            return Result.error("555","查无关系");
        }
        List<GeneMirnaRelationship> geneMirnaRelationships = geneMirnaRelationshipService.query().eq("mirna_name", mirnaName).list();
        return Result.success(geneMirnaRelationships);
    }

    @GetMapping("/GetByMirnaNameAndGene")
    @ApiOperation("通过mirna名字和基因来获取对应的关系")
    public Result<List<GeneMirnaRelationship>> GetByMirnaName(@RequestParam String mirnaName, @RequestParam String gene){
        if(mirnaName == null || gene == null){
            return Result.error("555","查无关系");
        }
        List<GeneMirnaRelationship> geneMirnaRelationships = geneMirnaRelationshipService.query().eq("mirna_name", mirnaName).eq("gene",gene).list();
        return Result.success(geneMirnaRelationships);
    }



}
