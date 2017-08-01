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
import html2pdf.html2pdf.itext.YtPDFComponent;
import html2pdf.html2pdf.itext.YtPDFWorkStreamInterface;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@Controller
@RequestMapping(value="/contract", method={RequestMethod.POST, RequestMethod.GET})
public class ContractController{


    private YtPDFWorkStreamInterface getWorkStraem (HttpServletResponse response){

       return new YtPDFWorkStreamInterface() {
            @Override
            public byte[] onTransformSuccess(byte[] bateArray) throws Exception {
                response.addHeader("Content-Length", "" + bateArray.length);
                return bateArray;
            }

            @Override
            public void onTransformComplete(byte[] bateArray) throws Exception {

            }

            @Override
            public void onTransformError(Exception e) {

            }
        };
    }

    @RequestMapping(value="/editTemplate")
    public ModelAndView edit(String id) {
        ModelAndView modelAndView = new ModelAndView("");
        return modelAndView;
    }


    @RequestMapping(value="/downloading")
    public void downloading(HttpServletResponse response, String pdfData) throws IOException, DocumentException {

        YtPDFComponent ytPDFComponent = new YtPDFComponent(this.getWorkStraem(response));

        Map map = this.getMap("11");

        String fileName = "attachment; filename=\"" + "测试" + ".pdf\"";

        fileName = new String(fileName.getBytes("utf-8"), "ISO8859-1").toUpperCase();

        response.setHeader("Content-Disposition",fileName );

        response.setContentType("application/octet-stream;charset=UTF-8");

        response.setCharacterEncoding("UTF-8");

        String aa = StringEscapeUtils.unescapeHtml4(pdfData);

        ytPDFComponent.ytSringToPDFStream(aa,response.getOutputStream(),map);

    }

    @RequestMapping(value = "/previewCodePdf")
    public void previewCodePdf(HttpServletResponse response, String pdfData) throws IOException, DocumentException {

        YtPDFComponent ytPDFComponent = new YtPDFComponent(this.getWorkStraem(response));

        Map map = this.getMap("后台的数据Id");

        String aa = StringEscapeUtils.unescapeHtml4(pdfData);

        ytPDFComponent.ytSringToPDFStream(aa,response.getOutputStream(),map);

    }


    public Map getMap(String id){

        Map map = new HashMap();

        map.put("orderId",id);

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
