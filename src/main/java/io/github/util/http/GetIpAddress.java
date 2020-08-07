package io.github.util.http;

import io.github.util.StringUtils;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 根据IP地址获取详细的地域信息
 *
 * @author Created by 思伟 on 2020/6/6
 */
public class GetIpAddress {

    /**
     * 判断字符串是否是一个IP地址
     *
     * @param addr 请求主机IP地址
     * @return boolean
     */
    public static boolean isIPAddr(String addr) {
        if (StringUtils.isEmpty(addr)) {
            return false;
        }
        String[] ips = StringUtils.split(addr, '.');
        if (ips.length != 4) {
            return false;
        }
        try {
            int ipa = Integer.parseInt(ips[0]);
            int ipb = Integer.parseInt(ips[1]);
            int ipc = Integer.parseInt(ips[2]);
            int ipd = Integer.parseInt(ips[3]);
            return ipa >= 0 && ipa <= 255 && ipb >= 0 && ipb <= 255 && ipc >= 0
                    && ipc <= 255 && ipd >= 0 && ipd <= 255;
        } catch (Exception e) {
        }
        return false;
    }

    /**
     * 是否内网ip地址
     *
     * @param addr 请求主机IP地址
     * @return boolean
     */
    public static boolean isLocalIPAddr(String addr) {
        return (addr.startsWith("10.") || addr.startsWith("192.168.") || "127.0.0.1".equals(addr));
    }

    /**
     * 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址;
     *
     * @param request HttpServletRequest
     * @return 请求主机IP地址
     * @throws IOException
     */
    public final static String getIpAddress(HttpServletRequest request) throws IOException {
        Assert.notNull(request, "request must not be null");
        // 获取请求主机IP地址,如果通过代理进来，则透过防火墙获取真实IP地址
        String ip = request.getHeader("X-Forwarded-For");
        String unknown = "unknown";
        if (StringUtils.isNotEmpty(ip) && !unknown.equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (StringUtils.contains(ip, ",")) {
                ip = ip.split(",")[0];
            }
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
        } else {
            int maxIpCheckLength = 15;
            if (StringUtils.isNotBlank(ip) && ip.length() > maxIpCheckLength) {
                String[] ips = ip.split(",");
                for (int index = 0; index < ips.length; index++) {
                    String strIp = ips[index];
                    if (!unknown.equalsIgnoreCase(strIp)) {
                        ip = strIp;
                        break;
                    }
                }
            }
        }
        return ip;
    }

    /**
     * 根据IP地址获取所在市区
     *
     * @param ip             ip地址 请求的参数 格式为：name=xxx&pwd=xxx
     * @param encodingString 服务器端请求编码。如GBK,UTF-8等
     * @return 所在市区
     * @throws IOException
     */
    public String getAddresses(String ip, String encodingString) throws IOException {
        // 这里调用pconline的接口
        String urlStr = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&ip=" + ip;
        // 从http://whois.pconline.com.cn取得IP所在的省市区信息
        String returnStr = this.getResult(urlStr, encodingString);
        if (returnStr != null) {
            // 处理返回的省市区信息
            String[] temp = returnStr.split(",");
            int minResultLength = 3;
            if (temp.length < minResultLength) {
                // 无效IP，局域网测试
                return "0";
            }
            String country = "";
            String region = "";
            String city = "";
            country = (temp[3].split(":"))[1].replaceAll("\"", "");
            // 国家
            country = decodeUnicode(country);
            region = (temp[4].split(":"))[1].replaceAll("\"", "");
            // 省份
            region = decodeUnicode(region);
            city = (temp[5].split(":"))[1].replaceAll("\"", "");
            // 市区
            city = decodeUnicode(city);
            return city;
        }
        return null;
    }

    /**
     * 请求URL
     *
     * @param urlStr   请求的地址
     * @param encoding 服务器端请求编码。如GBK,UTF-8等
     * @return String
     * @throws IOException
     */
    private String getResult(String urlStr, String encoding) throws IOException {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(urlStr);
            // 新建连接实例
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接超时时间，单位毫秒
            connection.setConnectTimeout(2000);
            // 设置读取数据超时时间，单位毫秒
            connection.setReadTimeout(2000);
            // 是否打开输出流 true|false
            connection.setDoOutput(true);
            // 是否打开输入流true|false
            connection.setDoInput(true);
            // 提交方法POST|GET
            connection.setRequestMethod("POST");
            // 是否缓存true|false
            connection.setUseCaches(false);
            // 打开连接端口
            connection.connect();
            // 打开输出流往对端服务器写数据
            try (DataOutputStream out = new DataOutputStream(connection.getOutputStream())) {
                // out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
                out.flush();
            }
            // 往对端写完数据对端服务器返回数据
            StringBuffer buffer;
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), encoding))) {
                // 以BufferedReader流来读取
                buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }
            return buffer.toString();
        } catch (IOException e) {
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();// 关闭连接
            }
        }
    }

    /**
     * Unicode 转换成 中文
     *
     * @param theString Unicode
     * @return String
     */
    public static String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    int length = 4;
                    for (int i = 0; i < length; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * 测试
     *
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        GetIpAddress addressUtils = new GetIpAddress();
        // 测试ip 219.136.134.157 中国=华南=广东省=广州市=越秀区=电信
        String ip = "61.235.13.252";
        String address = "";
        // 输出结果为：广东省,广州市,越秀区
        address = addressUtils.getAddresses(ip, StandardCharsets.UTF_8.name());
        System.out.println(address);

    }
}