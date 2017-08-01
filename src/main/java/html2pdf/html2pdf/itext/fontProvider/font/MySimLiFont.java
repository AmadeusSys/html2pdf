package html2pdf.html2pdf.itext.fontProvider.font;

import com.itextpdf.text.pdf.BaseFont;
import html2pdf.html2pdf.itext.fontProvider.MyFontProviderConent;
import html2pdf.html2pdf.itext.fontProvider.MyFontsProviderStatePattern;

public class MySimLiFont implements MyFontsProviderStatePattern {
    @Override
    public BaseFont doSelectFount(MyFontProviderConent myFontProviderConent) {

        if ("SIMLI".equals(myFontProviderConent.getFontName()) || "隶书".equals(myFontProviderConent.getFontName())){

            return myFontProviderConent.createBaseFont("SIMLI.TTF");

        }else{

            return myFontProviderConent.setState(new MyMsyhFont());

        }

    }
}
