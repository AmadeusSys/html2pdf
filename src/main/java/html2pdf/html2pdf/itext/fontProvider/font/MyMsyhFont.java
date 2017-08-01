package html2pdf.html2pdf.itext.fontProvider.font;

import com.itextpdf.text.pdf.BaseFont;
import html2pdf.html2pdf.itext.fontProvider.MyFontProviderConent;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProviderStatePattern;

public class MyMsyhFont implements MyFontsProviderStatePattern {
    @Override
    public BaseFont doSelectFount(MyFontProviderConent myFontProviderConent) {

        if ("MSYH".equals(myFontProviderConent.getFontName()) || "微软雅黑".equals(myFontProviderConent.getFontName())){

            return myFontProviderConent.createBaseFont("msyh.TTF");

        }else{

            return myFontProviderConent.setState(new MyDefaultFont());

        }

    }
}
