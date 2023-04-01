package mirna.stukk.utils;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-04-01 0:50
 **/
public class PrefixUtils {
    public static final String MiRNARecordKey = "searchRecord:MiRNA:";
    public static final String ArticleMiRNARecordKey = "searchRecord:ArticleMiRNA:"; //搜索文章页面的mirna
    public static final String ArticleDiseaseRecordKey = "searchRecord:ArticleDisease:"; //搜索文章页面的disease

    public static final String RankWeek = "RankWeek";
    public static final String RankMonth = "RankMonth";
    public static final String RankYear = "RankYear";


    public static void main(String[] args) {
        Date date = new Date();
        DateTime beginOfWeek = DateUtil.beginOfWeek(date);
        long dayLength = DateUtil.between(date, beginOfWeek, DateUnit.DAY) + 1; //获取这周多少天
        System.out.println(beginOfWeek+"-->"+dayLength);
    }
}
