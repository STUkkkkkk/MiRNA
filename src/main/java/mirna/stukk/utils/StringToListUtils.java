package mirna.stukk.utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-19 11:56
 **/
public class StringToListUtils {

    public static List<String> StringToList(String message){
        if(message == null){
            return null;
        }
        List<String> list = new LinkedList<>();
        String a[] = message.split("'");
        for(int i = 0;i<a.length;i++){
            if(a[i].length() > 1){
                list.add(a[i]);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        String authors = "['Zhang Haidi' 'Kolb Fabrice A' 'Brondani Vincent' 'Billy Eric' 'Filipowicz Witold']";
        String doi  ="['S0960-9822(02)01206-X [pii]' '10.1016/s0960-9822(02)01206-x [doi]']";
        String keywords = "['RNA activation' 'RNA interference' 'RNAome' 'Yin and Yang' 'drug discovery' 'functional genomics' 'gene silencing' 'small interfering RNA' 'therapeutics']";
        String types = "['Journal Article' \"Research Support Non-U.S. Gov't\" \"Research Support U.S. Gov't P.H.S.\"]";
        System.out.println(StringToList(doi));
        System.out.println(StringToList(authors));
        System.out.println(StringToList(keywords));
        System.out.println(StringToList(types));
    }

}
