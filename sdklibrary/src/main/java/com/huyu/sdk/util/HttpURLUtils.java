package com.huyu.sdk.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * @author chengzj
 * @time 2020/7/23 14:23
 * Description: HttpURLConnectionUtils
 */
public class HttpURLUtils {
    public static final String TAG = HttpURLUtils.class.getSimpleName();

    private HttpURLUtils() {
    }

    public static String post(final String url, final Map<String, String> params) throws Exception {
        String result = null;
        HttpURLConnection connection = null;
        OutputStream outputStream = null;
        URL u = new URL(url);
        connection = (HttpURLConnection) u.openConnection();
        // 设置输入可用
        connection.setDoInput(true);
        // 设置输出可用
        connection.setDoOutput(true);
        // 设置请求方式
        connection.setRequestMethod("POST");
        // 设置连接超时
        connection.setConnectTimeout(5000);
        // 设置读取超时
        connection.setReadTimeout(5000);
        // 设置缓存不可用
        connection.setUseCaches(false);
        // 开始连接
        connection.connect();

        // 只有当POST请求时才会执行此代码段
        if (params != null) {
            // 获取输出流,connection.getOutputStream已经包含了connect方法的调用
            outputStream = connection.getOutputStream();
            StringBuilder sb = new StringBuilder();
            Set<Map.Entry<String, String>> sets = params.entrySet();
            // 将Hashmap转换为string
            for (Map.Entry<String, String> entry : sets) {
                sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            String param = sb.substring(0, sb.length() - 1);
            // 使用输出流将string类型的参数写到服务器
            outputStream.write(param.getBytes());
            outputStream.flush();
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            result = inputStream2String(inputStream);
        } else {
            throw new HttpURLExeception();
        }
        connection.disconnect();
        outputStream.close();
        return result;
    }

    public static final class HttpURLExeception extends Exception {
        public HttpURLExeception() {
            super("HttpURL Exeception");
        }
    }

    /**
     * 字节流转换成字符串
     *
     * @param inputStream
     * @return
     */
    private static String inputStream2String(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(bytes)) != -1) {
                baos.write(bytes, 0, len);
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
