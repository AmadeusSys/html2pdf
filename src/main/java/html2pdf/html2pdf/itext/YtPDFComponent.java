package html2pdf.html2pdf.itext;

import com.itextpdf.awt.geom.Rectangle2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.tool.xml.Pipeline;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import html2pdf.html2pdf.controller.TestRenderListener;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProvider;
import org.jsoup.Jsoup;

import java.io.*;
import java.util.List;
import java.util.Map;

import static freemarker.template.Configuration.AUTO_DETECT_TAG_SYNTAX;

@SuppressWarnings("deprecation")
public class YtPDFComponent {

    private YtPDFWorkStreamInterface ytPDFWorkStreamInterface;

    public YtPDFComponent(YtPDFWorkStreamInterface ytPDFWorkStreamInterface){

        this.ytPDFWorkStreamInterface = ytPDFWorkStreamInterface;

    }


    /**
     * 获取完整网页信息
     * @param templateString 网页模板信息
     * @param data 数据
     * @return
     * @throws IOException
     * @throws TemplateException
     */
    private String ytGetHtmlString(String templateString,Map data) throws IOException, TemplateException {

        Configuration cfg = new Configuration();

        cfg.setTagSyntax(AUTO_DETECT_TAG_SYNTAX);
        //无视掉空制的异常
        cfg.setClassicCompatible(true);

        StringTemplateLoader stringLoader = new StringTemplateLoader();

        stringLoader.putTemplate("myTemplate",templateString);

        cfg.setTemplateLoader(stringLoader);

        StringWriter writer = new StringWriter();

        Template template = cfg.getTemplate("myTemplate","utf-8");

        template.process(data, writer);

        org.jsoup.nodes.Document doc = Jsoup.parse(writer.toString());

        return doc.toString();

    }

    /**
     * 获取完整的pdf数据流
     * @param html
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private byte[] ytGetPDFStream(String html) throws DocumentException, IOException {

        // step 1
        Document document = new Document();

        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = PdfWriter.getInstance(document,tempOutputStream);

        document.open();

        InputStream in_withcode =   new ByteArrayInputStream(html.getBytes("UTF-8"));

        //载入自定义解析器来解决中文不显示问题
        MyFontsProvider fontProvider = new MyFontsProvider();

        fontProvider.addFontSubstitute("lowagie", "garamond");

        fontProvider.setUseUnicode(true);

        //使用我们的字体提供器，并将其设置为unicode字体样式
        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

        HtmlPipelineContext htmlContext = new HtmlPipelineContext(cssAppliers);

        htmlContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

        CSSResolver cssResolver = XMLWorkerHelper.getInstance() .getDefaultCssResolver(true);

        Pipeline<?> pipeline = new CssResolverPipeline(cssResolver, new HtmlPipeline(htmlContext, new PdfWriterPipeline(document, writer)));

        XMLWorker worker = new XMLWorker(pipeline, true);

        XMLParser p = new XMLParser(worker);

        p.parse(new InputStreamReader(in_withcode, "UTF-8"));

        document.close();

        return tempOutputStream.toByteArray();

    }

    public void ytStringToPDFStream(String templateString, OutputStream outputStream, Map data) throws DocumentException {

        byte[] newPdfBate = null;

        try {

            String html = this.ytGetHtmlString(templateString, data);

            byte[] pdfBate = this.ytGetPDFStream(html);

            newPdfBate = this.ytPDFWorkStreamInterface.onTransformSuccess(pdfBate);

            outputStream.write(newPdfBate);

        } catch (Exception e) {

            e.printStackTrace();

            this.ytPDFWorkStreamInterface.onTransformError(e);

        } finally {

            try {
                this.ytPDFWorkStreamInterface.onTransformComplete(newPdfBate);
            } catch (Exception e) {
                e.printStackTrace();
                this.ytPDFWorkStreamInterface.onTransformError(e);
            }
        }

    }

}
