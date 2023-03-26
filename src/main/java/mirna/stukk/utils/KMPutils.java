package mirna.stukk.utils;

import io.swagger.models.auth.In;
import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

public class KMPutils {

    public static void main(String[] args) {
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
        String s = "hsa-mir";
        long pre = System.currentTimeMillis();
        String s1 = HighLight(p, s);
        System.out.println(s1);
        long now = System.currentTimeMillis();
        System.out.println((now - pre)+"ms");
    }


    public static String HighLight(String p,String s){
        List<Integer> kmp = KMP(p, s);
        int po = 0;
        for(int i : kmp){
            int start = i + po;
            int end = i + po + s.length();
            p = p.substring(0,start)+"<em class=\"highlight\">"+p.substring(start,end)+"</em>"+p.substring(end);
            po += ("<em class=\"highlight\">"+"</em>").length();
        }
        return p;
    }

    public static List<Integer> KMP(String p,String s){
        if(p.length() < s.length()){
            return null;
        }
        int next[] = getNext(s);
        return solve(next,p,s);
    }

    public static int[] getNext(String s){
        int next[] = new int[s.length()];
        if(s == null || s.length() == 0){
            return next;
        }
        if(s.length() == 1){
            next[0] = -1;
            return next;
        }
        else{
            next[0] = -1;
            next[1] = 0;
            int pi = 2;
            int tmp = 0;
            while(pi < s.length()){
                if(s.charAt(pi - 1) == s.charAt(tmp)){
                    next[pi++] = ++tmp;
                }
                else if(tmp > 0){
                    tmp = next[tmp];
                }
                else{
                    next[pi++] = 0;
                }
            }
            return next;
        }
    }

    public static List<Integer> solve(int [] next,String p,String s){
        int x = 0;
        int y = 0;
        List<Integer> ans = new LinkedList<>();
        int pi = 0;
        while(x < p.length() && y < s.length()){
            if(p.charAt(x) == s.charAt(y)){
                x++;
                y++;
            }
            else if(next[y] < 0){
                x++;
            }
            else{
                y = next[y];
            }
            if(y == s.length()){
                ans.add(x - y) ;
                y = 0;
            }
        }
        return ans;
    }


}
