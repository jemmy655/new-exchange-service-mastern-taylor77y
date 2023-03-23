package cn.xa87.data.tool;
import java.io.*;
import java.net.MalformedURLException;
import java.util.Map;

public class HttpReques {
    public static String sendPost(String url, Map<String, String> mapParams) throws IOException {
        String result = "";
        BufferedReader in = null;
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer();
        String params = "";
        try {
            if (mapParams.size() == 1) {
                for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8"));
                }
                params = sb.toString();
            } else {
                for (Map.Entry<String, String> entry : mapParams.entrySet()) {
                    sb.append(entry.getKey()).append("=").append(
                            entry.getValue()==null?null:
                                    java.net.URLEncoder.encode(entry.getValue().toString(),
                                            "UTF-8")).append("&");
                }
                params = sb.toString().substring(0, sb.toString().length() - 1);
            }

            java.net.URL connURL = new java.net.URL(url);

            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();

            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            httpConn.setRequestProperty("Accept-Charset", "utf-8");

            httpConn.setRequestMethod("POST");

            httpConn.setDoInput(true);

            httpConn.setDoOutput(true);

            httpConn.setUseCaches(false);

            httpConn.setConnectTimeout(30000);

            httpConn.setReadTimeout(30000);
            out = new PrintWriter(httpConn.getOutputStream());
            out.write(params);
            out.flush();
            int code = httpConn.getResponseCode();
            System.out.println("code:"+code);
            if(code==200) {
                in = new BufferedReader(new InputStreamReader(httpConn
                        .getInputStream(), "UTF-8"));
                String line;
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        }  finally {
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

}
