package html2pdf.html2pdf.itext;

public class YtPDFWorkStream implements YtPDFWorkStreamInterface {
    @Override
    public byte[] onTransformSuccess(byte[] bateArray) {
        return bateArray;
    }

    @Override
    public void onTransformComplete(byte[] bateArray) throws Exception {

    }

    @Override
    public void onTransformError(Exception e) {

    }

}
