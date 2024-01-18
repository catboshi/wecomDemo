package tech.wedev.wecom.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import tech.wedev.wecom.exception.WecomException;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpRequestUtil {

    public static JSONObject getAccessResult(String url) throws IOException {
        log.info("HttpTools GET########url: " + url);
        JSONObject result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet httpGet = new HttpGet(url);
            CloseableHttpResponse response = httpClient.execute(httpGet);
            try {
                //获取响应实体
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    String content = EntityUtils.toString(entity, "utf-8");
                    result = JSONObject.parseObject(content);
                    log.info("HttpTools GET########出参【result】" + result);
                }
            } finally {
                response.close();
            }
        } catch (IOException ioException) {
            log.error("HttpTools GET######## IOException", "{}", ioException);
            throw new WecomException(500, "response close IOException: " + ioException.getMessage());
        } catch (Exception e) {
            log.error("HttpTools GET######## Exception", "{}", e);
            throw new WecomException(500, "Exception: " + e.getMessage());
        } finally {
            httpClient.close();
        }
        return result;
    }

    public static String httpPost(String url,String dataSource) throws IOException {
        log.info("HttpTools POST########url: " + url + "######dataSource: " + dataSource);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        return httpPostSponsor(httpClient, url, dataSource);
    }

    public static String httpPost(String url,String dataSource,Integer timeout) throws IOException {
        log.info("HttpTools POST########url: " + url + "######dataSource: " + dataSource);
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setSocketTimeout(timeout)              //数据传输的超时时间
                .setConnectTimeout(timeout)             //连接超时时间
                .setConnectionRequestTimeout(timeout)   //从连接池中获取连接的最长时间
                .build();
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        return httpPostSponsor(httpClient, url, dataSource);
    }

    public static String httpPostSponsor(CloseableHttpClient httpClient,String url,String dataSource) throws IOException {
        //创建httpPost
        HttpPost httpPost = new HttpPost(url);
        //接收报文类型
        httpPost.setHeader("Accept", "application/json");
        //发送报文类型
        httpPost.setHeader("Content-Type", "application/json;charset=utf-8");
        //dsf服务网关迁移校验
        httpPost.setHeader("X-Request-App", "F-CFBI-FAM");
        if (dataSource != null && !"".equals(dataSource)) {
            StringEntity entity = new StringEntity(dataSource, "UTF-8");
            entity.setContentEncoding("UTF-8");
            httpPost.setEntity(entity);
        }
        CloseableHttpResponse response = null;
        String jsonString = null;
        try {
            response = httpClient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                jsonString = EntityUtils.toString(responseEntity, "UTF-8");
                log.info("HttpTools POST########出参【jsonString】" + jsonString);
            }
        } catch (ClientProtocolException e) {
            log.error("HttpTools POST######## ClientProtocolException", "{}", e);
            throw new WecomException(500, "ClientProtocolException: " + e.getMessage());
        } catch (IOException e) {
            log.error("HttpTools POST######## IOException", "{}", e);
            throw new WecomException(500, "IOException: " + e.getMessage());
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return jsonString;
    }

    public static String httpRequestFileStream(String requestUrl, String fileName, Long fileLength, byte[] fileStream) {
        String res = null;
        StringBuffer buffer = new StringBuffer();
        if (StringUtil.isEmpty(requestUrl) || StringUtil.isEmpty(fileName) || fileLength == null || fileStream == null) {
            log.error("发送POST请求上传文件流出错###，以下参数必传：requestUrl/fileName/fileLength/fileStream");
            Map<String, String> tmp = new HashMap<>();
            tmp.put("errcode", "400");
            tmp.put("errmsg", "上传文件缺失必要参数");
            res = JSON.toJSONString(tmp);
            buffer.append(res);
            return buffer.toString();
        }

        HttpURLConnection conn = null;

        try {
            //获得连接
            conn = configConnection(requestUrl);
            String BOUNDARY = "-----------CFBI|FILE|BOUNDARY-----------";

            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            //将文件头输出到微信服务器
            StringBuilder builder = new StringBuilder();
            builder.append("--");
            builder.append(BOUNDARY);
            builder.append("\r\n");
            builder.append("Content-Disposition: form-data; name=\""
                    + "media" + "\"; filename=\"" + fileName + "\"; filelength=\"" + fileLength
                    + "\"\r\n");
            builder.append("Content-Type: application/octet-stream" + "\r\n\r\n");
            byte[] head = builder.toString().getBytes("utf-8");

            //获得输出流
            OutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(head);
            outputStream.write(fileStream);

            byte[] foot = ("--" + BOUNDARY + "--" + "\r\n").getBytes("utf-8");
            outputStream.write(foot);
            outputStream.flush();
            outputStream.close();
            //将返回的输入流转换成字符串

            int responseCode = conn.getResponseCode();
            String responseMessage = conn.getResponseMessage();
            if (responseCode != 200) {
                //读取返回数据
                Map<String, String> tmp = new HashMap<>();
                tmp.put("errcode", String.valueOf(responseCode));
                tmp.put("errmsg", "http error: " + responseMessage);
                res = JSON.toJSONString(tmp);
                buffer.append(res);
            } else {
                InputStream inputStream = conn.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String str = null;
                while ((str = bufferedReader.readLine()) != null) {
                    buffer.append(str);
                }
                bufferedReader.close();
                inputStreamReader.close();
                inputStream.close();
            }
        } catch (Exception e) {
            log.error("发送POST请求上传文件出错###" + e);
            Map<String, String> tmp = new HashMap<>();
            tmp.put("errcode", String.valueOf(1));
            tmp.put("errmsg", "httpClient url connect error!");
            res = JSON.toJSONString(tmp);
            buffer.append(res);
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return buffer.toString();
    }

    private static HttpURLConnection configConnection(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(30000);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows; U; Windows NT 6.1; zh-CN; rv:1.9.2.6)");
        String AccountPassword = "admin:admin";
        String basicAuth = "Basic " + DatatypeConverter.printBase64Binary(AccountPassword.getBytes());
        conn.setRequestProperty("Authorization", basicAuth);
        //post请求缓存设为false
        conn.setRequestProperty("Cache-Control", "no-cache");
        //设置该HttpURLConnection实例是否自动执行重定向
        conn.setInstanceFollowRedirects(true);
        return conn;
    }
}
