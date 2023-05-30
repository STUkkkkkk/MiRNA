package mirna.stukk.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.Disease;
import mirna.stukk.Pojo.MiRNA;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.MirnaMapper;
import mirna.stukk.service.MirnaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mirna")
@Api(tags = "mirna接口")
public class MiRNAController {

    @Autowired
    private MirnaMapper mirnaMapper;

    @Autowired
    private MirnaService mirnaService;

    @GetMapping("/GetLikeName")
    @ApiOperation("根据MirnaName模糊查询所有的mirna")
    public Result<List<MiRNA>> GetLikeName(@RequestParam String MirnaName){
        List<MiRNA> miRNAS = mirnaMapper.selectList(Wrappers.<MiRNA>lambdaQuery().like(MiRNA::getName, MirnaName));
        return Result.success(miRNAS);
    }


    @GetMapping("/all")
    @ApiOperation("获取所有的miRNA")
    public Result<List<MiRNA>> getAll(){
        List<MiRNA> list = mirnaService.query().list();
        return Result.success(list);
    }


    @GetMapping("/getById/{id}")
    @ApiOperation("根据id获取miRNA")
    public Result<MiRNA> getById(@PathVariable Long id){
        MiRNA miRNA = mirnaService.getById(id);
        return Result.success(miRNA);
    }

    @GetMapping("/getByName")
    @ApiOperation("根据名字获取Mirna")
    public Result<MiRNA> getById(@RequestParam String mirnaName){
        MiRNA miRNA = mirnaService.query().eq("mirna_name",mirnaName).one();
        return Result.success(miRNA);
    }





}