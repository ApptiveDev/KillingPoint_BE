package apptive.team5.global.util;

public class S3Util {

    public static String s3Url;

    public static void setS3Url(String url) {
        s3Url = url;
    }

    public static String extractFileName(String fileUrl) {
        int pathStartIdx = fileUrl.contains(".amazonaws.com/") ?
                fileUrl.indexOf(".amazonaws.com/") + ".amazonaws.com/".length() : 0;

        int pathEndIdx = fileUrl.contains("?") ? fileUrl.indexOf("?") : fileUrl.length();

        return fileUrl.substring(pathStartIdx, pathEndIdx).trim();
    }

}
