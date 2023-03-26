package mirna.stukk.utils;

/**
 * @Author: stukk
 * @Description: TODO  doi设置转化
 * @DateTime: 2023-03-22 15:38
 **/
public class DoiUtils {

    private static String pre = "https://doi.org/";

    public static void main(String[] args) {
        String p = "['232 [pii]' '10.4161/cc.2.1.232 [doi]']";
        System.out.println(ToUrl(p));
    }
    public static String ToDoi(String p){ //转成doi
//        String pre = "https://doi.org/";
        if(p == null){
            return null;
        }
        String a[] = p.split("'");
        for(String s : a){
            if(s.length() > 1 && s.contains("[doi]")){
                return s.substring(0,s.length()-6);
            }
        }
        return null;
    }
    public static String ToUrl(String p){
        if(p == null){
            return null;
        }
        return pre+ToDoi(p);
    }

}
