/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package html2pdf.html2pdf.controller;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;
import html2pdf.html2pdf.itext.YtPDFComponent;
import html2pdf.html2pdf.itext.YtPDFWorkStreamInterface;
import html2pdf.html2pdf.seal.*;
import html2pdf.html2pdf.seal.configuration.SealCircle;
import html2pdf.html2pdf.seal.configuration.SealConfiguration;
import html2pdf.html2pdf.seal.configuration.SealFont;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@Controller
@RequestMapping(value="/contract", method={RequestMethod.POST, RequestMethod.GET})
public class ContractController{


    private YtPDFWorkStreamInterface getWorkStream (HttpServletResponse response){

       return new YtPDFWorkStreamInterface() {
            @Override
            public byte[] onTransformSuccess(byte[] bateArray) throws Exception {
//                response.addHeader("Content-Length", "" + bateArray.length);

                char[] password = "123456".toCharArray();
                Resource resource = new ClassPathResource("document/test.p12");
                KeyStore ks = KeyStore.getInstance("PKCS12");
                ks.load(resource.getInputStream(), password);
                String alias = (String) ks.aliases().nextElement();
                PrivateKey pk = (PrivateKey) ks.getKey(alias, password);
                Certificate[] chain = ks.getCertificateChain(alias);
                ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
                //目标文件输出流
                //创建签章工具PdfStamper ，最后一个boolean参数
                //false的话，pdf文件只允许被签名一次，多次签名，最后一次有效
                //true的话，pdf可以被追加签名，验签工具可以识别出每次签名之后文档是否被修改
                PdfReader reader = new PdfReader(bateArray);
                PdfStamper stamper = PdfStamper.createSignature(reader, tempOutputStream, '\0', null, false);

                // 获取数字签章属性对象，设定数字签章的属性
                PdfSignatureAppearance appearance = stamper.getSignatureAppearance();
                appearance.setReason("测试位置");
                appearance.setLocation("签名点");
                //设置签名的位置，页码，签名域名称，多次追加签名的时候，签名预名称不能一样
                //签名的位置，是图章相对于pdf页面的位置坐标，原点为pdf页面左下角
                //四个参数的分别是，图章左下角x，图章左下角y，图章右上角x，图章右上角y
                appearance.setVisibleSignature(new Rectangle(0, 0, 100, 100), 1, "sig1");
                //读取图章图片，这个image是itext包的image
                Image image = Image.getInstance(asf());
                appearance.setSignatureGraphic(image);
                appearance.setCertificationLevel(PdfSignatureAppearance.CERTIFIED_NO_CHANGES_ALLOWED);
                //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
                appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

                // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
                // 摘要算法
                ExternalDigest digest = new BouncyCastleDigest();
                // 签名算法
                ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA256, null);
                // 调用itext签名方法完成pdf签章CryptoStandard.CMS 签名方式，建议采用这种
                MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

                return tempOutputStream.toByteArray();
            }

            @Override
            public void onTransformComplete(byte[] bateArray) throws Exception {

            }

            @Override
            public void onTransformError(Exception e) {

            }
        };
    }

    public byte[] asf() throws Exception {

        SealConfiguration configuration = new SealConfiguration();

        /**
         * 添加主文字
         */
        configuration.setMainFont(new SealFont() {{
            setBold(true);
            setFontFamily("楷体");
            setMarginSize(10);
            setFontText("沈阳享牛逼技有限责任公司");
            setFontSize(35);
            setFontSpace(35.0);
        }});
        /**
         * 添加副文字
         */
        configuration.setViceFont(new SealFont() {{
            setBold(true);
            setFontFamily("宋体");
            setMarginSize(5);
            setFontText("123456789012345");
            setFontSize(13);
            setFontSpace(12.0);
        }});
        /**
         * 添加中心文字
         */
        configuration.setCenterFont(new SealFont() {{
            setBold(true);
            setFontFamily("宋体");
            setFontText("★ ");
            setFontSize(100);
            setFontSpace(0.0);
            setMarginSize(0);
        }});

        /**
         * 图片大小
         */
        configuration.setImageSize(300);
        /**
         * 背景颜色
         */
        configuration.setBackgroudColor(Color.RED);
        /**
         * 边线粗细、半径
         */
        configuration.setBorderCircle(new SealCircle(10, 140, 140));
        /**
         * 内边线粗细、半径
         */
        configuration.setBorderInnerCircle(new SealCircle(1, 135, 135));
//        configuration.setBorderInnerCircle(new SealCircle(1, 135, 95));
        /**
         * 内环线粗细、半径
         */
//        configuration.setInnerCircle(new SealCircle(2, 105, 105));

        BufferedImage bufferedImage = SealUtil.buildSeal(configuration);

        return SealUtil.buildBytes(bufferedImage);
    }

    @RequestMapping(value="/editTemplate")
    public ModelAndView edit(String id) {
        ModelAndView modelAndView = new ModelAndView("");
        return modelAndView;
    }


    @RequestMapping(value="/downloading")
    public void downloading(HttpServletResponse response, String firstParty, String secondParty) throws IOException, DocumentException {

        YtPDFComponent ytPDFComponent = new YtPDFComponent(this.getWorkStream(response));

        Map map = this.getMap(firstParty, secondParty);

        String fileName = "attachment; filename=\"" + "测试" + ".pdf\"";

        fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1").toUpperCase();

        response.setHeader("Content-Disposition",fileName );

        response.setContentType("application/octet-stream;charset=UTF-8");

        response.setCharacterEncoding("UTF-8");

        String aa = StringEscapeUtils.unescapeHtml4(this.getDocument());

        ytPDFComponent.ytStringToPDFStream(aa,response.getOutputStream(),map);

    }

    @RequestMapping(value = "/previewCodePdf")
    public void previewCodePdf(HttpServletResponse response, String firstParty, String secondParty) throws IOException, DocumentException {

        YtPDFComponent ytPDFComponent = new YtPDFComponent(this.getWorkStream(response));

        Map map = this.getMap(firstParty, secondParty);

        String aa = StringEscapeUtils.unescapeHtml4(this.getDocument());

        ytPDFComponent.ytStringToPDFStream(aa,response.getOutputStream(),map);

    }

    public String getDocument() throws IOException {

        Resource resource = new ClassPathResource("document/LaborContract.html");

        FileReader reader = new FileReader(resource.getFile());

        BufferedReader bReader = new BufferedReader(reader);

        StringBuilder sb = new StringBuilder();

        String s = "";

        while ((s =bReader.readLine()) != null) {
            sb.append(s + "\n");
        }
        bReader.close();
        return sb.toString();
    }


    public Map getMap(String firstParty, String secondParty){

        Map map = new HashMap();

        map.put("FirstParty", StringUtils.isNotEmpty(firstParty) ? firstParty : "默认甲方");
        map.put("SecondParty", StringUtils.isNotEmpty(secondParty) ? secondParty : "默认乙方");

        String dateString = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        map.put("DateString", dateString);

        ArrayList arrayList = new ArrayList();

        for (int i = 0; i < 10; i++) {
            Map map1 = new HashMap();
            map1.put("id",i);
            map1.put("agre",18);
            map1.put("name","列表名字"+i);
            map1.put("type","类型"+i);
            arrayList.add(map1);
        }

        map.put("listArray",arrayList);

        return map;
    }


}
