package mirna.stukk.Pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_article")
public class Article implements Serializable {
    @TableId
    private Long pmid;

    private String title;
    private String type;
    private String authors;
    private String keywords;
    private String doi;
    private String library;
    private String abs;
    private String date;
}

/*
论文id ---- pmid
论文标题----title
类型-----type
作者 ----- authors
关键字------keywords
doi ------ doi
来自的图书馆----library
摘要-----abstract
发布日期-----date
 */