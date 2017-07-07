package com.taylor.common.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import javax.ws.rs.core.MediaType;
import java.io.*;
import java.net.*;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2
public class HTTPClientUtil {

    private static final JsonNodeFactory factory = new JsonNodeFactory(false);

    private static final X509HostnameVerifier verifier = new MyX509HostnameVerifier();

    /**
     * Send SSL Request
     *
     * @return
     */
    public static ObjectNode sendHTTPRequest(String urlString, Credential credential, Object dataBody, String method) {

        HttpClient httpClient = getClient(true);
        URI uri = getURI(urlString);
        ObjectNode resObjectNode = factory.objectNode();

        try {
            StringEntity entity = new StringEntity(String.valueOf(dataBody), "UTF-8");
            if (log.isDebugEnabled()) {
                log.debug("[sendHTTPRequest]:{}", String.valueOf(dataBody));
            }
            HttpResponse response = null;

            if (method.equals(HTTPMethod.METHOD_POST)) {
                HttpPost httpPost = new HttpPost(uri);

                if (credential != null) {
                    Token.applyAuthentication(httpPost, credential);
                }
                httpPost.setEntity(entity);

                response = httpClient.execute(httpPost);
            } else if (method.equals(HTTPMethod.METHOD_PUT)) {
                HttpPut httpPut = new HttpPut(uri);
                if (credential != null) {
                    Token.applyAuthentication(httpPut, credential);
                }
                httpPut.setEntity(entity);

                response = httpClient.execute(httpPut);
            } else if (method.equals(HTTPMethod.METHOD_GET)) {

                HttpGet httpGet = new HttpGet(uri);
                if (credential != null) {
                    Token.applyAuthentication(httpGet, credential);
                }

                response = httpClient.execute(httpGet);

            } else if (method.equals(HTTPMethod.METHOD_DELETE)) {
                HttpDelete httpDelete = new HttpDelete(uri);

                if (credential != null) {
                    Token.applyAuthentication(httpDelete, credential);
                }

                response = httpClient.execute(httpDelete);
            }

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UtilityRuntimeException(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return resObjectNode;
    }

    public static void inputstreamtofile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            try {
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }
                os.close();
                ins.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new UtilityRuntimeException(e.getMessage(), e);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            throw new UtilityRuntimeException(e.getMessage(), e);
        }

    }

    /**
     * Send SSL Request
     *
     * @return
     */
    public static ObjectNode sendHTTPRequest(String urlString, Object dataBody, String method) {

        HttpClient httpClient = getClient(true);
        URI uri = getURI(urlString);
        ObjectNode resObjectNode = factory.objectNode();

        try {
            StringEntity entity = new StringEntity(String.valueOf(dataBody), "UTF-8");
            HttpResponse response = null;

            if (method.equals(HTTPMethod.METHOD_POST)) {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(entity);

                response = httpClient.execute(httpPost);
            } else if (method.equals(HTTPMethod.METHOD_PUT)) {
                HttpPut httpPut = new HttpPut(uri);
                httpPut.setEntity(entity);

                response = httpClient.execute(httpPut);
            } else if (method.equals(HTTPMethod.METHOD_GET)) {

                HttpGet httpGet = new HttpGet(uri);
                response = httpClient.execute(httpGet);

            } else if (method.equals(HTTPMethod.METHOD_DELETE)) {
                HttpDelete httpDelete = new HttpDelete(uri);
                response = httpClient.execute(httpDelete);
            }

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                if (resObjectNode == null) {
                    resObjectNode = HTTPClientUtil.factory.objectNode();
                }
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            String errMsg = String
                    .format("Exception when sendHTTPRequest with url %s, dataBody %s and method %s: %s",
                            urlString, String.valueOf(dataBody), method,
                            e.getMessage());
            log.error(errMsg, e);
            throw new UtilityRuntimeException(errMsg, e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return resObjectNode;
    }

    /**
     * Send SSL Request
     *
     * @return
     */
    public static ObjectNode sendHTTPRequest(String urlString, Object dataBody, String method, Map<String, String> headers) {

        HttpClient httpClient = getClient(true);
        URI uri = getURI(urlString);
        ObjectNode resObjectNode = factory.objectNode();

        try {
            StringEntity entity = new StringEntity(String.valueOf(dataBody), "UTF-8");
            HttpResponse response = null;

            if (method.equals(HTTPMethod.METHOD_POST)) {
                HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(entity);
                if (headers != null && !headers.isEmpty()) {
                    for (String key : headers.keySet()) {
                        httpPost.addHeader(key, headers.get(key));
                    }
                }

                response = httpClient.execute(httpPost);
            } else if (method.equals(HTTPMethod.METHOD_PUT)) {
                HttpPut httpPut = new HttpPut(uri);
                httpPut.setEntity(entity);
                if (headers != null && !headers.isEmpty()) {
                    for (String key : headers.keySet()) {
                        httpPut.addHeader(key, headers.get(key));
                    }
                }

                response = httpClient.execute(httpPut);
            } else if (method.equals(HTTPMethod.METHOD_GET)) {

                HttpGet httpGet = new HttpGet(uri);
                if (headers != null && !headers.isEmpty()) {
                    for (String key : headers.keySet()) {
                        httpGet.addHeader(key, headers.get(key));
                    }
                }
                response = httpClient.execute(httpGet);

            } else if (method.equals(HTTPMethod.METHOD_DELETE)) {
                HttpDelete httpDelete = new HttpDelete(uri);
                if (headers != null && !headers.isEmpty()) {
                    for (String key : headers.keySet()) {
                        httpDelete.addHeader(key, headers.get(key));
                    }
                }
                response = httpClient.execute(httpDelete);
            }

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            String errMsg = String
                    .format("Exception when sendHTTPRequest with url %s, dataBody %s and method %s: %s",
                            urlString, String.valueOf(dataBody), method,
                            e.getMessage());
            log.error(errMsg, e);
            throw new UtilityRuntimeException(errMsg, e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return resObjectNode;
    }

    /**
     * DownLoadFile whit Jersey
     *
     * @return
     * @throws java.security.NoSuchAlgorithmException
     * @throws java.security.KeyManagementException
     * @throws IOException
     */
    public static File downLoadFile(URL url, Credential credential, List<NameValuePair> headers, File localPath) {

        HttpClient httpClient = getClient(true);

        try {

            HttpGet httpGet = new HttpGet(url.toURI());

            if (credential != null) {
                Token.applyAuthentication(httpGet, credential);
            }
            for (NameValuePair header : headers) {
                httpGet.addHeader(header.getName(), header.getValue());
            }

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            FileOutputStream fos = new FileOutputStream(localPath);

            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }

            fos.close();

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UtilityRuntimeException(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return localPath;
    }

    /**
     * UploadFile whit Jersey
     *
     * @return
     */
    public static ObjectNode uploadFile(URI url, File file, Credential credential, List<NameValuePair> headers)
            throws RuntimeException {
        HttpClient httpClient = getClient(true);

        ObjectNode resObjectNode = factory.objectNode();

        try {
            HttpPost httpPost = new HttpPost(url);

            if (credential != null) {
                Token.applyAuthentication(httpPost, credential);
            }
            for (NameValuePair header : headers) {
                httpPost.addHeader(header.getName(), header.getValue());
            }

            MultipartEntity mpEntity = new MultipartEntity();
            ContentBody cbFile = new FileBody(file, MediaType.APPLICATION_OCTET_STREAM);
            mpEntity.addPart("file", cbFile);
            httpPost.setEntity(mpEntity);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String responseContent = EntityUtils.toString(entity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new UtilityRuntimeException(e.getMessage(), e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }

        return resObjectNode;
    }

    /**
     * Create a httpClient instance
     *
     * @param isSSL
     * @return HttpClient instance
     */
    public static HttpClient getClient(boolean isSSL) {

        HttpClient httpClient = new DefaultHttpClient();
        if (isSSL) {
            X509TrustManager xtm = new MyX509TrustManager();

            try {
                SSLContext ctx = SSLContext.getInstance("TLS");
                ctx.init(null, new TrustManager[]{xtm}, null);


                SSLSocketFactory socketFactory = new SSLSocketFactory(ctx, verifier);

                httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));

            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new UtilityRuntimeException(e.getMessage(), e);
            }
        }

        return httpClient;
    }

    public static URI getURI(String path) {
        URI uri = null;


        try {
            URL url = new URL(path);
            if (url.getPort() > 0) {
                uri = new URI(url.getProtocol(), null, url.getHost(), url.getPort(), url.getPath(), url.getQuery(), null);
            } else {
                uri = new URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), null);
            }

        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        }

        return uri;
    }

    /**
     * Check illegal String
     *
     * @param regex
     * @param str
     * @return
     */
    public static boolean match(String regex, String str) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        return matcher.lookingAt();
    }

    public static URL getURL(String format) {
        // TODO Auto-generated method stub
        URL url = null;


        try {
            url = new URL(format);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static ObjectNode postHttpRequest(String urlString, Object dataBody,
                                             List<NameValuePair> headers) {
        HttpClient httpClient = getClient(true);
        URI uri = getURI(urlString);
        ObjectNode resObjectNode = factory.objectNode();
        String responseContent = null;
        try {
            StringEntity entity = new StringEntity(String.valueOf(dataBody), "UTF-8");
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);
            if (headers != null) {
                for (NameValuePair nameValuePair : headers) {
                    httpPost.addHeader(nameValuePair.getName(), nameValuePair.getValue());
                }
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();

            if (responseEntity != null) {
                responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            String msg = String.format(
                    "Exception met when send request to %s with dataBody %s and the response is: %s",
                    urlString, String.valueOf(dataBody), responseContent);
            log.error(msg, e);
            throw new UtilityRuntimeException(msg, e);
        }
        return resObjectNode;
    }


    public static ObjectNode postHttpFormData(String urlString, List<NameValuePair> parameters) {
        HttpClient httpClient = getClient(true);
        URI uri = getURI(urlString);
        ObjectNode resObjectNode = factory.objectNode();

        try {
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(parameters, "UTF-8");
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(formEntity);

            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(formEntity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resObjectNode;
    }

    public static ObjectNode postHttpJsonData(String url, Object dataBody) {
        HttpClient httpClient = getClient(true);
        URI uri = getURI(url);
        ObjectNode resObjectNode = factory.objectNode();

        try {
            StringEntity entity = new StringEntity(JSON.toJSONString(dataBody), "UTF-8");
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setEntity(entity);
            httpPost.addHeader(HTTP.CONTENT_TYPE, "application/json");
            //entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
            HttpResponse response = httpClient.execute(httpPost);

            HttpEntity responseEntity = response.getEntity();
            if (responseEntity != null) {
                String responseContent = EntityUtils.toString(responseEntity, "UTF-8");
                EntityUtils.consume(entity);

                ObjectMapper mapper = new ObjectMapper();
                JsonFactory factory = mapper.getJsonFactory();
                JsonParser jp = factory.createJsonParser(responseContent);

                resObjectNode = mapper.readTree(jp);
                if (resObjectNode == null) {
                    resObjectNode = HTTPClientUtil.factory.objectNode();
                }
                resObjectNode.put("statusCode", response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resObjectNode;
    }

    public static ObjectNode postHttpFormData(String urlString, ObjectNode formValues) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        Iterator<String> iterator = formValues.fieldNames();
        while (iterator.hasNext()) {
            String fieldName = iterator.next();
            JsonNode valueNode = formValues.get(fieldName);
            String fieldValue = null;
            if (valueNode instanceof ObjectNode) {
                //TODO
            } else {
                fieldValue = valueNode.asText();
            }
            NameValuePair pare =
                    new BasicNameValuePair(fieldName, fieldValue);
            parameters.add(pare);
        }
        return postHttpFormData(urlString, parameters);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是Map的形式,请在key为参数，value为值。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, Map<String, Object> param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            HttpURLConnection httpUrlConn = (HttpURLConnection) conn;
            // 设置通用的请求属性
            httpUrlConn.setRequestProperty("accept", "*/*");
            httpUrlConn.setRequestProperty("connection", "Keep-Alive");
            httpUrlConn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setRequestMethod("POST");

            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpUrlConn.getOutputStream());
            // 发送请求参数
            out.print(getParam(param));
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(httpUrlConn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }

        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 构建请求参数
     *
     * @param params
     * @return
     */
    public static String getParam(Map<String, Object> params) {
        StringBuffer sb = new StringBuffer();
        if (params != null) {
            for (Entry<String, Object> e : params.entrySet()) {
                sb.append(e.getKey());
                sb.append("=");
                sb.append(e.getValue().toString());
                sb.append("&");
            }
            sb.substring(0, sb.length() - 1);
        }
        return sb.toString();
    }

    public static void main(String... args) {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        NameValuePair username = new BasicNameValuePair("username", "devtest002");
        NameValuePair password = new BasicNameValuePair("password", "000000");
        parameters.add(username);
        parameters.add(password);
        ObjectNode response = postHttpFormData("http://112.124.28.82:6005/login", parameters);
        System.out.println(response);

        Map<String, String> HTTP_API_HEADERS = new HashMap<String, String>();
        HTTP_API_HEADERS.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        String dataBody = "{\"good_name\":\"微盟智慧餐厅测试-支付测试\",\"modules_code\":\"O2O\",\"notify_url\":\"http://test.o2o.com\",\"openid\":\"oUA7tvowWrqJgQiYAPkS2BXZYJ_Q\",\"order_data\":\"\",\"order_sn\":\"1492415352856\",\"out_trade_no\":\"1492415352856\",\"pc_id\":23050750,\"pid\":800000656,\"total_price\":\"0.01\",\"trade_type\":\"wap\"}";
        System.out.println(dataBody);
        ObjectNode response2 = sendHTTPRequest("http://120.26.142.104:8086/core/pc/payment/pay", dataBody, "POST", HTTP_API_HEADERS);
        System.out.println(response2);

    }

    private static class MyX509HostnameVerifier implements X509HostnameVerifier {
        @Override
        public void verify(String host, SSLSocket ssl) throws IOException {

        }

        @Override
        public void verify(String host, X509Certificate cert) throws SSLException {

        }

        @Override
        public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {

        }

        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    }

    private static class MyX509TrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }
}

