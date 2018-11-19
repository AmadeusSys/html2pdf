package html2pdf.html2pdf.itext.fontProvider;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.BaseFont;

import java.io.IOException;

public class MyFontProviderConent {

    private String fontName;

    private String FONTPATH = this.getClass().getResource("/").toString()+"static/font/";

    public static MyFontProviderConent init(String fontName){
        MyFontProviderConent myFontProviderConent = new MyFontProviderConent();
        myFontProviderConent.fontName = fontName;
        return myFontProviderConent;
    }

    public BaseFont setState(MyFontsProviderStatePattern myFontsProviderStatePattern){
        return myFontsProviderStatePattern.doSelectFount(this);
    }

    public BaseFont createBaseFont(String fontName){

        BaseFont bfChinese = null;

        try {
            bfChinese = BaseFont.createFont(FONTPATH + fontName, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bfChinese;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName.toUpperCase();
    }
}
