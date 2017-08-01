package html2pdf.html2pdf.itext.fontProvider;

import com.itextpdf.text.pdf.BaseFont;

public interface MyFontsProviderStatePattern {
    BaseFont doSelectFount(MyFontProviderConent myFontProviderConent);
}
