package html2pdf.html2pdf.itext;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.security.*;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public class YtPDFSign {

    private String signImagePath = "";
    private String signKeyStorePath = "";
    private char[] signPassWord = "".toCharArray();

    private float llx = 450;

    private float lly = 50;

    private float urx = 550;

    private float ury = 150;

    private Boolean isAppendSign = true;

    private int signPage = 1;

    public YtPDFSign(String keyStorePath,String passWord,String imagePath){

        this.signKeyStorePath = keyStorePath;

        this.signPassWord = passWord.toCharArray();

        this.signImagePath = imagePath;

    }

    public byte[] ytSign(byte[] PdfData,String reason) throws IOException, GeneralSecurityException, DocumentException {

        ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();

        PdfReader reader = new PdfReader(PdfData);

        KeyStore ks = KeyStore.getInstance("PKCS12");

        ks.load(new FileInputStream(signKeyStorePath), signPassWord);

        String alias = (String)ks.aliases().nextElement();

        PrivateKey pk = (PrivateKey) ks.getKey(alias, signPassWord);

        Certificate[] chain = ks.getCertificateChain(alias);

        PdfStamper stamper = PdfStamper.createSignature(reader, tempOutputStream, '\0', null, isAppendSign);

        PdfSignatureAppearance appearance = stamper.getSignatureAppearance();

        //签名理由
        appearance.setReason(reason);

        //签章位置
        appearance.setVisibleSignature(new Rectangle(llx, lly, urx, ury), signPage, "sig1");

        //设置图片
        appearance.setSignatureGraphic(Image.getInstance(signImagePath));

        appearance.setCertificationLevel(PdfSignatureAppearance.NOT_CERTIFIED);

        //设置图章的显示方式，如下选择的是只显示图章（还有其他的模式，可以图章和签名描述一同显示）
        appearance.setRenderingMode(PdfSignatureAppearance.RenderingMode.GRAPHIC);

        // 这里的itext提供了2个用于签名的接口，可以自己实现，后边着重说这个实现
        ExternalDigest digest = new BouncyCastleDigest();
        // 签名算法
        ExternalSignature signature = new PrivateKeySignature(pk, DigestAlgorithms.SHA1, null);
        // 调用itext签名方法完成pdf签章
        MakeSignature.signDetached(appearance, digest, signature, chain, null, null, null, 0, MakeSignature.CryptoStandard.CMS);

        return tempOutputStream.toByteArray();

    }

    public float getLlx() {
        return llx;
    }

    public void setLlx(float llx) {
        this.llx = llx;
    }

    public float getLly() {
        return lly;
    }

    public void setLly(float lly) {
        this.lly = lly;
    }

    public float getUrx() {
        return urx;
    }

    public void setUrx(float urx) {
        this.urx = urx;
    }

    public float getUry() {
        return ury;
    }

    public void setUry(float ury) {
        this.ury = ury;
    }

    public int getSignPage() {
        return signPage;
    }

    public void setSignPage(int signPage) {
        this.signPage = signPage;
    }

    public String getSignImagePath() {
        return signImagePath;
    }

    public void setSignImagePath(String signImagePath) {
        this.signImagePath = signImagePath;
    }

    public String getSignKeyStorePath() {
        return signKeyStorePath;
    }

    public void setSignKeyStorePath(String signKeyStorePath) {
        this.signKeyStorePath = signKeyStorePath;
    }

    public char[] getSignPassWord() {
        return signPassWord;
    }

    public void setSignPassWord(char[] signPassWord) {
        this.signPassWord = signPassWord;
    }

    public Boolean getAppendSign() {
        return isAppendSign;
    }

    public void setAppendSign(Boolean appendSign) {
        isAppendSign = appendSign;
    }
}
