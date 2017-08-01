package html2pdf.html2pdf.itext;

public interface YtPDFWorkStreamInterface {

    byte[] onTransformSuccess(byte[] bateArray) throws Exception;

    void onTransformComplete(byte[] bateArray) throws Exception;

    void onTransformError(Exception e);

}
