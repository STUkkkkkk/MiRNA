package mirna.stukk.controller;

import cn.hutool.core.date.LocalDateTimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.*;
import mirna.stukk.config.Result;
import mirna.stukk.mapper.MirnaMapper;
import mirna.stukk.service.GeneMirnaRelationshipService;
import mirna.stukk.utils.PrefixUtils;
import org.elasticsearch.index.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-30 0:16
 **/
@RestController
@RequestMapping("/GeneMirnaRelationship")
@Api(tags = "基因和MiRNA的关系和关系图的接口")
public class GeneMirnaRelationshipController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private GeneMirnaRelationshipService geneMirnaRelationshipService;
    @Autowired
    private MirnaMapper mirnaMapper;

    @GetMapping("/getAllMiRNA")
    @ApiOperation("获取全部可以用来搜索MiRNA-基因的MiRNA名字")
    public Result<List<MiRNA>> getAllMiRNA(){
        List<MiRNA> searchMiRNA = mirnaMapper.getAllSearchMiRNA();
        return Result.success(searchMiRNA);
    }

    @GetMapping("/getLikeMiRNA")
    @ApiOperation("模糊搜索可以用来搜索MiRNA-基因的MiRNA名字")
    public Result<List<MiRNA>> getLikeMiRNA(@RequestParam String mirnaName){
        List<MiRNA> searchMiRNA = mirnaMapper.getLikeMiRNA("%"+mirnaName+"%");
        return Result.success(searchMiRNA);
    }

    @GetMapping("/GetByMirnaName")
    @ApiOperation("通过mirna名字来获取对应的基因关系图")
    public Result<GeneDiagram> GetByMirnaName(@RequestParam String mirnaName){
        if(mirnaName == null){
            return Result.error("555","查无关系");
        }
        List<GeneMirnaRelationship> geneMirnaRelationships = geneMirnaRelationshipService.query().eq("mirna_name", mirnaName).list();
        if(geneMirnaRelationships == null){
            return Result.success(null);
        }
        //获取Mirna包含的只因以及来源
        List<GeneNode> geneNodes = new LinkedList<>();
        List<Link> links = new LinkedList<>();
        List<Category> categories = new LinkedList<>();
        Integer index = 0;
        //加入第一个点，也就是mirna
        geneNodes.add(GeneNode.builder().id(index.toString()).name(mirnaName).category(0).value(1).symbolSize(25).build());
        categories.add(new Category("MiRNA: "+mirnaName));
        index++; //下标加1
        HashMap<String, Integer> map = new HashMap<>();
        Integer pi = 1; //因为已经加了一条MIRNA的类型数据了，所以从1开始
        for(GeneMirnaRelationship geneMirnaRelationship : geneMirnaRelationships){
            //找一下关联的图
            String cellLine = geneMirnaRelationship.getCellLine();
            if(!map.containsKey(cellLine)){
                categories.add(new Category(cellLine));
                map.put(cellLine,pi++);
            }
            geneNodes.add(GeneNode.builder().geneMirnaRelationship(geneMirnaRelationship).id(index.toString()).value(1).category(map.get(cellLine))
                    .name(geneMirnaRelationship.getGene()).symbolSize(15).build());
            links.add(Link.builder().target("0").source(index.toString()).build());
            index++;
        }
        String key = PrefixUtils.MiRNARecordKey + LocalDateTimeUtil.format(LocalDateTime.now(),"yyyy-MM-dd");
        redisTemplate.opsForZSet().incrementScore(key,mirnaName,1);
        return Result.success(GeneDiagram.builder().geneNodes(geneNodes).links(links).categories(categories).build());
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
