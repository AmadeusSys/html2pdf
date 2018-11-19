package html2pdf.html2pdf.itext.fontProvider;

import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import html2pdf.html2pdf.itext.fontProvider.font.MySimkaiFont;

public class MyFontsProvider extends XMLWorkerFontProvider{

    public MyFontsProvider(){
        super(null,null);
    }

    @Override
    public Font getFont(final String fontName, String encoding, float size, final int style) {

        MyFontProviderConent myFontProviderConent = MyFontProviderConent.init(fontName);

        BaseFont baseFont = myFontProviderConent.setState(new MySimkaiFont());

        return new Font(baseFont, size, style);

    }

}
