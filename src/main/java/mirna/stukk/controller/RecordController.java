package mirna.stukk.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import mirna.stukk.Pojo.DTO.RecordDTO;
import mirna.stukk.Pojo.Record;
import mirna.stukk.config.Result;
import mirna.stukk.utils.PrefixUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-01 0:40
 **/
@RestController
@RequestMapping("/record")
@Api(tags = "获取搜索记录的接口")
public class RecordController {

    @Autowired
    private RedisTemplate redisTemplate;


    @GetMapping("/getTodayMiRNATopN/{n}")
    @ApiOperation("获取日榜（今日）搜索mirna结构的前N排名的mirna名字")
    public Result<Map<String,Object>> getTopMiRNA(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        String key = PrefixUtils.MiRNARecordKey + localDateTime.format(DateTimeFormatter.ISO_DATE);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }

    private Map<String,Object> change(List<RecordDTO> recordDTOList){
        Map<String,Object> map = new HashMap<>();
        List<String> searchName = recordDTOList.stream().map(RecordDTO::getSearchName).collect(Collectors.toList());
        List<Long >times = recordDTOList.stream().map(RecordDTO::getTimes).collect(Collectors.toList());
        map.put("searchName",searchName);
        map.put("times",times);
        return map;
    }


    @GetMapping("/getWeekMiRNATopN/{n}")
    @ApiOperation("获取周榜（本周）搜索mirna结构的前N排名的mirna名字")
    public Result<Map<String,Object>> getWeekMiRNATopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        long dayLength = DateUtil.between(date, beginOfWeek, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.MiRNARecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfWeek,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.MiRNARecordKey+PrefixUtils.RankWeek);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.MiRNARecordKey+PrefixUtils.RankWeek,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }

    @GetMapping("/getMonthMiRNATopN/{n}")
    @ApiOperation("获取月榜（本月）搜索mirna结构的前N排名的mirna名字")
    public Result<Map<String,Object>> getMonthMiRNATopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfMonth = DateUtil.beginOfMonth(date);
        long dayLength = DateUtil.between(date, beginOfMonth, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.MiRNARecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfMonth,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.MiRNARecordKey+PrefixUtils.RankMonth);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.MiRNARecordKey+PrefixUtils.RankMonth,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }
//------------------------Article:MiRNA------------------------------------


    @GetMapping("/getTodayArticleMiRNATopN/{n}")
    @ApiOperation("获取日榜（今日）搜索文章的mirna的前N排名的mirna名字")
    public Result<Map<String,Object>> getTodayArticleMiRNATopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        String key = PrefixUtils.ArticleMiRNARecordKey + localDateTime.format(DateTimeFormatter.ISO_DATE);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }



    @GetMapping("/getWeekArticleMiRNATopN/{n}")
    @ApiOperation("获取周榜（本周）搜索文章页面的mirna的前N排名的mirna名字")
    public Result<Map<String,Object>> getWeekArticleMiRNATopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        long dayLength = DateUtil.between(date, beginOfWeek, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.ArticleMiRNARecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfWeek,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.ArticleMiRNARecordKey+PrefixUtils.RankWeek);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.ArticleMiRNARecordKey+PrefixUtils.RankWeek,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }

    @GetMapping("/getMonthArticleMiRNATopN/{n}")
    @ApiOperation("获取月榜（本月）搜索文章页面的mirna的前N排名的mirna名字")
    public Result<Map<String,Object>> getMonthArticleMiRNATopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfMonth = DateUtil.beginOfMonth(date);
        long dayLength = DateUtil.between(date, beginOfMonth, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.ArticleMiRNARecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfMonth,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.ArticleMiRNARecordKey+PrefixUtils.RankMonth);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.ArticleMiRNARecordKey+PrefixUtils.RankMonth,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }
    //------------------------Article:Disease------------------------------------


    @GetMapping("/getTodayArticleDiseaseTopN/{n}")
    @ApiOperation("获取日榜（今日）搜索文章的疾病的前N排名的疾病名字")
    public Result<Map<String,Object>> getTodayArticleDiseaseTopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        LocalDateTime localDateTime = LocalDateTime.now();
        String key = PrefixUtils.ArticleDiseaseRecordKey + localDateTime.format(DateTimeFormatter.ISO_DATE);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(key, 0, n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }



    @GetMapping("/getWeekArticleDiseaseTopN/{n}")
    @ApiOperation("获取周榜（本周）搜索文章页面的疾病的前N排名的疾病名字")
    public Result<Map<String,Object>> getWeekArticleDiseaseTopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        long dayLength = DateUtil.between(date, beginOfWeek, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.ArticleDiseaseRecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfWeek,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.ArticleDiseaseRecordKey+PrefixUtils.RankWeek);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.ArticleDiseaseRecordKey+PrefixUtils.RankWeek,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }

    @GetMapping("/getMonthArticleDiseaseTopN/{n}")
    @ApiOperation("获取月榜（本月）搜索文章页面的疾病的前N排名的疾病名字")
    public Result<Map<String,Object>> getMonthArticleDiseaseTopN(@PathVariable Long n){
        if(n < 1){
            return Result.error("555","获取的排名小于1，没意义");
        }
        List<RecordDTO> recordDTOList = new LinkedList<>();
        Date date = new Date();
        DateTime beginOfMonth = DateUtil.beginOfMonth(date);
        long dayLength = DateUtil.between(date, beginOfMonth, DateUnit.DAY) + 1; //获取这周多少天
        List<String> days = new LinkedList<>();
        for(int i = 0;i<dayLength;i++){
            days.add(PrefixUtils.ArticleDiseaseRecordKey+DateUtil.formatDate(DateUtil.offsetDay(beginOfMonth,i)));
        }
        redisTemplate.opsForZSet().unionAndStore("kkkk",days,PrefixUtils.ArticleDiseaseRecordKey+PrefixUtils.RankMonth);

        Set<ZSetOperations.TypedTuple<String>> set = redisTemplate.opsForZSet().reverseRangeWithScores(PrefixUtils.ArticleDiseaseRecordKey+PrefixUtils.RankMonth,0,n-1);
        JSONArray jsonArray = JSONObject.parseArray(JSONObject.toJSONString(set));
        if(jsonArray == null){
            return Result.success(null);
        }
        for(int i = 0, size = jsonArray.size(); i < size; i++) {
            RecordDTO recordDTO = new RecordDTO();
            JSONObject o = JSONObject.parseObject(jsonArray.get(i).toString());
            recordDTO.setSearchName(o.getString("value"));
            recordDTO.setTimes(o.getLongValue("score"));
            recordDTOList.add(recordDTO);
        }
        return Result.success(change(recordDTOList));
    }
}
/*

 */
