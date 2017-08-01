package html2pdf.html2pdf.itext.fontProvider.font;

import com.itextpdf.text.pdf.BaseFont;
import html2pdf.html2pdf.itext.fontProvider.MyFontProviderConent;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProviderStatePattern;

public class MySimHeiFont implements MyFontsProviderStatePattern {
    @Override
    public BaseFont doSelectFount(MyFontProviderConent myFontProviderConent) {

        if ("SIMHEI".equals(myFontProviderConent.getFontName()) || "黑体".equals(myFontProviderConent.getFontName())){

            return myFontProviderConent.createBaseFont("simhei.ttf");

        }else{

            return myFontProviderConent.setState(new MySimLiFont());

        }

    }
}
