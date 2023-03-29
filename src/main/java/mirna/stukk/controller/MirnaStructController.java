package mirna.stukk.controller;

import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.MirnaStruct;
import mirna.stukk.config.Result;
import mirna.stukk.service.MirnaStructService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
