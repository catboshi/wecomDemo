package tech.wedev.wecom.tools;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 根据URL路径读取资源内容
 */
@Slf4j
public class HttpProxyTools {
    private HttpProxyTools() {

    }

    public static boolean getFileByHttpUrl(String url, String savePath) {
        boolean result = true;
        InputStream is = null;
        FileOutputStream fileOutputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL httpUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            fileOutputStream = new FileOutputStream(savePath);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("###HttpProxyTools.getFileByHttpUrl###读取资源内容异常！", e);
            result = false;
        } finally {
            closeInputStream(is);
            closeFileOutputStream(fileOutputStream);
            closeHttpURLConnection(httpURLConnection);
        }
        return result;
    }

    public static byte[] getFileByHttpUrl(String url) {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        HttpURLConnection httpURLConnection = null;
        try {
            os = new ByteArrayOutputStream();
            URL httpUrl = new URL(url);
            httpURLConnection = (HttpURLConnection) httpUrl.openConnection();
            httpURLConnection.connect();
            is = httpURLConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error("###HttpProxyTools.getFileByHttpUrl###读取资源内容异常！", e);
        } finally {
            closeInputStream(is);
            closeByteArrayOutputStream(os);
            closeHttpURLConnection(httpURLConnection);
        }
        return os.toByteArray();
    }

    public static boolean closeInputStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("closeInputStream流关闭异常！", e);
        }
        return true;
    }

    public static boolean closeFileOutputStream(FileOutputStream fileOutputStream) {
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (Exception e) {
            log.error("closeFileOutputStream流关闭异常！", e);
        }
        return true;
    }

    public static boolean closeHttpURLConnection(HttpURLConnection httpURLConnection) {
        try {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        } catch (Exception e) {
            log.error("closeHttpURLConnection连接关闭异常！", e);
        }
        return true;
    }

    public static boolean closeByteArrayOutputStream(ByteArrayOutputStream bos) {
        try {
            if (bos != null) {
                bos.close();
            }
        } catch (Exception e) {
            log.error("closeByteArrayOutputStream流关闭异常！", e);
        }
        return true;
    }
}
