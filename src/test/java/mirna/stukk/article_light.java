package mirna.stukk;


import mirna.stukk.Pojo.Article;
import mirna.stukk.Pojo.DTO.ArticleDTO;
import mirna.stukk.config.Result;
import mirna.stukk.service.ArticleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@SpringBootTest
@RunWith(SpringRunner.class)
public class article_light {

    @Test
    public void Highlight(){
        String p = "MicroRNAs (miRNAs) are increasingly being shown to play vital roles in development" +
                " apoptosis and oncogenesis by interfering with gene expression at the post-transcriptional level." +
                " miRNAs in principle can contribute to the repertoire of host pathogen interactions during HIV-1 infection." +
                " Using a consensus scoring approach high scoring miRNA-target pairs were selected which were identified" +
                " by four well-established target prediction softwares. While hsa-mir-29a and 29b target the nef " +
                "gene hsa-mir-149 targets the vpr gene hsa-mir-378 targets env and hsa-mir-324-5p targets the vif gene. Not only" +
                " were the minimum free energy values lowest for the bound complex but also the rules so far observed for microRNA-target" +
                " pairing viz. a continuous stretch of 6-7 base pairing towards the 5' end of the miRNA and incomplete complementarity with " +
                "the target sequence were found to be valid. The target regions were highly conserved across the various clades of HIV-1. microRNA " +
                "expression profiles from previously reported microarray based experiments show that the five human miRNAs are expressed in T-cells " +
                "the normal site of infection of " +
                "HIV-1 virus. This is the first report of human microRNAs which can target crucial HIV-1 genes including" +
                " the nef gene which plays an important role in delayed disease progression.";
        String s = "hsa-mir-29a";
        boolean contains = p.contains(s);
        int i = p.indexOf(s);
        int j = i+s.length();
        System.out.println(p.substring(0,i) +"<em class=\"highlight\">"+p.substring(i,j)+"</em>"+p.substring(j));
    }

    @Autowired
    private ArticleService articleService;

    @Test
    public void test() throws IOException {
        Result<ArticleDTO> byPmid = articleService.getByPmid(15738415L);
        System.out.println(byPmid);
    }



}
