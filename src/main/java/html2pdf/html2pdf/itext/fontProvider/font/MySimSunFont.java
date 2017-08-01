package html2pdf.html2pdf.itext.fontProvider.font;

import com.itextpdf.text.pdf.BaseFont;
import html2pdf.html2pdf.itext.fontProvider.MyFontProviderConent;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProviderStatePattern;

public class MySimSunFont implements MyFontsProviderStatePattern {
    @Override
    public BaseFont doSelectFount(MyFontProviderConent myFontProviderConent) {

        if ("SIMSUN".equals(myFontProviderConent.getFontName()) || "宋体".equals(myFontProviderConent.getFontName())){

            return myFontProviderConent.createBaseFont("simsun.ttc,0");

        }else{

            return myFontProviderConent.setState(new MySimHeiFont());

        }

    }
}
