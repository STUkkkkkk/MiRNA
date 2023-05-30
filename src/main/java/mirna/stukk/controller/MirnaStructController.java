package mirna.stukk.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.DTO.MirnaAllMessageDTO;
import mirna.stukk.Pojo.MirnaStruct;
import mirna.stukk.Pojo.MirnaAllMessage;
import mirna.stukk.Pojo.Mirna_3P_5P;
import mirna.stukk.config.Result;
import mirna.stukk.service.MirnaStructService;
import mirna.stukk.service.MirnaAllMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-29 21:00
 **/
@RestController
@RequestMapping("/mirnaStruct")
@Api(tags = "关于MiRNA的结构接口")
public class MirnaStructController {

    @Autowired
    private MirnaStructService mirnaStructService;
    @Autowired
    private MirnaAllMessageService mirnaAllMessageService;
    @Value("${Prefix2D}")
    private String Prefix2d;
    @Value("${Prefix3D}")
    private String Prefix3d;

    @GetMapping("/GetByMirnaName")
    @ApiOperation(value = "根据mrina的名字来获取结果")
    public Result<MirnaStruct> GetByMirnaName(@RequestParam String mirnaName){
        if(mirnaName == null){
            return Result.error("555","查无此MiRNA的结构图");
        }
        MirnaStruct mirnaStruct = mirnaStructService.query().eq("mirna_name", mirnaName).one();
        if(mirnaStruct == null){
            return Result.error("555","查无此MiRNA的结构图");
        }
        return Result.success(mirnaStruct);
    }

    @PostMapping("/GetByMirnaNameList")
    @ApiOperation(value = "根据好几个mirna的名字来获取mirna的结构图")
    public Result<List<MirnaStruct>> GetByMirnaNameList(@RequestBody List<String> mirnaNameList){
        if(mirnaNameList == null){
            return Result.error("555","查无此MiRNA的结构图");
        }
        List<MirnaStruct> mirnaStructList = mirnaStructService.query().in("mirna_name",mirnaNameList).list();
        if(mirnaStructList == null){
            return Result.error("555","查无此MiRNA的结构图");
        }
        return Result.success(mirnaStructList);
    }

    @GetMapping("/getAllMessage")
    @ApiOperation(value = "根据Mirna的名字获取所有的信息，包括2D、3D图，还有信息，以及3p-5p信息")
    public Result<MirnaAllMessageDTO> getAllMessageByName(@RequestParam String mirnaName){
        MirnaAllMessage mirna = mirnaAllMessageService.query().eq("mirna_name", mirnaName).one();
        if(mirna == null){
            return Result.success();
        }
        mirna.setStructUrl_2D(Prefix2d+mirna.getUrl());
        mirna.setStructUrl_3D(Prefix3d+mirna.getUrl());
        String mirna_3PName = mirnaName + "-3p";
        String mirna_5PName = mirnaName + "-5p";
        Mirna_3P_5P mirna_3P = mirnaAllMessageService.getBy3p5pName(mirna_3PName);
        Mirna_3P_5P mirna_5P = mirnaAllMessageService.getBy3p5pName(mirna_5PName);
        Mirna_3P_5P mainMirna = Mirna_3P_5P.builder().accession(mirna.getAccession()).cdna(mirna.getCdna()).family(mirna.getFamily()).genomeBuildAccession(mirna.getGenomeBuildAccession())
                        .genomeBuildId(mirna.getGenomeBuildId()).length(mirna.getLength()).microRNAs(mirna.getMicroRNAs()).mirnaName(mirna.getMirnaName())
                        .sequence(mirna.getSequence()).species(mirna.getSpecies()).build();
        mirna.setMirna_3P(mirna_3P);
        mirna.setMirna_5P(mirna_5P);

        MirnaAllMessageDTO mirnaAllMessageDTO = MirnaAllMessageDTO.builder().StructUrl_3D(mirna.getStructUrl_3D()).StructUrl_2D(mirna.getStructUrl_2D())
                .mirna_3P(mirna.getMirna_3P()).mirna_5P(mirna.getMirna_5P()).mainMirna(mainMirna).url(mirna.getUrl()).build();
        return Result.success(mirnaAllMessageDTO);
    }

    @GetMapping("/getMirnaLike")
    @ApiOperation("模糊查询hsa-mirna的接口")
    public Result<List<String>> getMirnaByLike(@RequestParam String mirnaName){
        mirnaName = mirnaName.trim();
        List<MirnaAllMessage> mirnaAllMessageList = mirnaAllMessageService.query().like("mirna_name", mirnaName).list();
        List<String> mirnaNames = mirnaAllMessageList.stream().map(MirnaAllMessage::getMirnaName).collect(Collectors.toList());
        return Result.success(mirnaNames);
    }

}
