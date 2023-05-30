package mirna.stukk.utils;


import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.collections.map.HashedMap;
import org.apache.velocity.Template;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

import java.io.OutputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @Author: stukk
 * @Description: TODO
 * @DateTime: 2023-03-25 19:46
 **/
public class PdfUtil {

    static {
        String cookie = "d_c0=\"ACCapA9ishKPTsgf9yiZs17mQy3ed2ulckY=|1613988729\"; _xsrf=AtgiBlvupVElErZErNmLy2YqXUPwbROf; _9755xjdesxxd_=32; YD00517437729195%3AWM_TID=kgsn9frIgd1FVVBQFVI6gDxOgt%2BAE1PG; q_c1=a20ff476a3244e778182424eb6e206d7|1645157170000|1645157170000; __snaker__id=oBwjO6gH5zwdRZ1d; YD00517437729195%3AWM_NI=oshSfaLIHZuWq4XKxSdy8c%2Fm6L%2FS7%2BuDUPZUXZICrric%2B6POwtlP2J0aN7ZTqCO0uDuNd8CmN4R3PsachNh%2BX0intvFhy0%2F62r1VIH03yz9KLSebTvZmGhRK9k2MHLnZaXU%3D; YD00517437729195%3AWM_NIKE=9ca17ae2e6ffcda170e2e6eed9f3808db3a599b745a1a88aa3d44e828a9eacd5529aef008cf7439397a1bbf92af0fea7c3b92aadada8b4e56b909788bbf85db3ec98acfb59a8a7f884ae67f694e1b4b621b79c9bb1d95f9c8687a4f17aabbde5d7d345b5aca4b1b142f4bf82d8d960b39d81d1f850b792acd9c2678eec9ed4d379a3bda8afeb219cf0a78ff77e86b8b7d1ae3ef2aeffb7cc44f4edb79bbc41908dab83d77e9ab38989d141fbb5feaecb48ae86afb9d837e2a3; _zap=d72749ce-7b35-4263-a01b-0785ba463532; z_c0=2|1:0|10:1684305721|4:z_c0|80:MS4xdFhrQ0h3QUFBQUFtQUFBQVlBSlZUZC12UkdWUHdZa3hJTlAyUENnX3lRbjhRRFdyaDFCUG1nPT0=|37c37c7cc9dd576cc60fb2fbc33cfb463b92823d461bdc2871b5e742419ba4a8; Hm_lvt_98beee57fd2ef70ccdd5ca52b9740c49=1684061277,1684216475,1684325069,1684467496; tst=r; Hm_lpvt_98beee57fd2ef70ccdd5ca52b9740c49=1684642989; SESSIONID=j1uwHOWVxM1oNYfLRz5goPaWCntdo7FwZk32rL7aoaw; JOID=UlEXA0moolwGp40oFqh5xCez75EGy5grc-3LdELS5mZr69ZHUQY3NWKsjyEWnwchcGvKVY80Yutu3KKx9woxUl0=; osd=W18SBEuhrFkBpYQmE697zSm26JMPxZ0sceTFcUXQ72hu7NROXwMwN2uiiiYUlgkkd2nDW4ozYOJg2aWz_gQ0VV8=; KLBRSID=4efa8d1879cb42f8c5b48fe9f8d37c16|1684642991|1684642484\n";
        // Velocity初始化
        Velocity.setProperty(RuntimeConstants.OUTPUT_ENCODING, StandardCharsets.UTF_8);
        Velocity.setProperty(RuntimeConstants.INPUT_ENCODING, StandardCharsets.UTF_8);
        Velocity.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        Velocity.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        Velocity.init();

        /*





         */




    }


    /**
     * 据模板生成pfd格式文件
     *
     * @param context      上下文对象
     * @param template     pdf模板
     * @param outputStream 生成ofd文件输出流
     */
    public static void pdfFile(Context context, String template, OutputStream outputStream) {
        try (PdfWriter pdfWriter = new PdfWriter(outputStream)) {
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.setDefaultPageSize(PageSize.A4); //设置页面大小 为A4

            ConverterProperties properties = new ConverterProperties();
            FontProvider fontProvider = new FontProvider();
            // 字体设置，解决中文不显示问题
                PdfFont sysFont = PdfFontFactory.createFont("MHei-Medium", "UniCNS-UCS2-H");
            fontProvider.addFont(sysFont.getFontProgram(), "UniCNS-UCS2-H");
            properties.setFontProvider(fontProvider);
//            设置好字体
            Template pfdTemplate = Velocity.getTemplate(template, "UTF-8");
            StringWriter writer = new StringWriter();
            pfdTemplate.merge(context, writer);
            HtmlConverter.convertToPdf(writer.toString(), pdfDocument, properties);
            pdfDocument.close();
        } catch (Exception e) {
            throw new RuntimeException("PFD文件生成失败", e);
        }
    }

}

