package cn.cs.callme.sdk;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class FloatIconProcessor implements Callable<Boolean> {
    private static final int CONNECT_TIMEOUT_IN_MILLISECONDS = 3000;
    private static final int READ_TIMEOUT_IN_MILLISECONDS = 3000;
    private String appKey;

    public FloatIconProcessor(String appKey) {
        this.appKey = appKey;
    }

    /**
     * @return
     * @throws IOException
     */
    private URLConnection urlConnectionForEventData(String serverUrl) throws IOException {
        final URL url = new URL(serverUrl + "?appId=" + appKey);
        final HttpURLConnection conn;
        conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(CONNECT_TIMEOUT_IN_MILLISECONDS);
        conn.setReadTimeout(READ_TIMEOUT_IN_MILLISECONDS);
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");

        return conn;
    }

    @Override
    public Boolean call() {
        URLConnection conn = null;
        while (true) {
            try {
                // initialize and open connection
                conn = urlConnectionForEventData("http://sc.cs.cn/showad.php");
                conn.connect();

                // response code has to be 2xx to be considered a success
                boolean success = true;
                final int responseCode;
                if (conn instanceof HttpURLConnection) {
                    final HttpURLConnection httpConn = (HttpURLConnection) conn;
                    responseCode = httpConn.getResponseCode();
                    success = responseCode >= 200 && responseCode < 300;
                } else {
                    responseCode = 0;
                }

                // HTTP response code was good, check response JSON contains {"result":"Success"}
                if (success) {
                    StringBuffer sb = new StringBuffer();
                    //得到读取的内容(流)
                    InputStreamReader in = new InputStreamReader(conn.getInputStream());
                    // 为输出创建BufferedReader
                    BufferedReader buffer = new BufferedReader(in);
                    String inputLine = null;
                    //使用循环来读取获得的数据
                    while (((inputLine = buffer.readLine()) != null)) {
                        //我们在每一行后面加上一个"\n"来换行
                        sb.append(inputLine);
                    }
                    //关闭InputStreamReader
                    in.close();
                    //
                    Log.e("得到读取的内容1", sb.toString());
                    //
                    if (sb.toString().toLowerCase().equals("true")) {
                        return Boolean.valueOf(true);
                    }else{
                        return Boolean.valueOf(false);
                    }
                } else if (responseCode >= 400 && responseCode < 500) {
                    return Boolean.valueOf(false);
                } else {
                    // warning was logged above, stop processing, let next loadData take care of retrying
                    return Boolean.valueOf(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                // if exception occurred, stop processing, let next loadData take care of retrying
                return Boolean.valueOf(false);
            } finally {
                // free connection resources
                if (conn != null && conn instanceof HttpURLConnection) {
                    ((HttpURLConnection) conn).disconnect();
                }
            }
        }

    }
}
