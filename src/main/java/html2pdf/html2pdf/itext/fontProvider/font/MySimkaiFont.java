package html2pdf.html2pdf.itext.fontProvider.font;

import com.itextpdf.text.pdf.BaseFont;
import html2pdf.html2pdf.itext.fontProvider.MyFontProviderConent;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProviderStatePattern;

public class MySimkaiFont implements MyFontsProviderStatePattern {
    @Override
    public BaseFont doSelectFount(MyFontProviderConent myFontProviderConent) {

        if ("SIMKAI".equals(myFontProviderConent.getFontName()) || "楷体".equals(myFontProviderConent.getFontName())){

            return myFontProviderConent.createBaseFont("simkai.ttf");

        }else{

            return myFontProviderConent.setState(new MySimSunFont());

        }

    }
}
