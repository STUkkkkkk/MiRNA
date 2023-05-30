package mirna.stukk.utils;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-05-23 11:03
 **/
public class FileUtil {

    /**
     * 获取文件名的后缀，如：changlu.jpg => .jpg
     * @return 文件后缀名
     */
    public static String getFileSuffix(String fileName) {
        return fileName.contains(".") ? fileName.substring(fileName.indexOf('.')) : null;
    }
}

