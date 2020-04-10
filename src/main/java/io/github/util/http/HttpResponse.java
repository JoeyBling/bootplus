/*
 * 微信公众平台(JAVA) SDK
 *
 * Copyright (c) 2014, Ansitech Network Technology Co.,Ltd All rights reserved.
 *
 * http://www.weixin4j.org/sdk/
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.util.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.github.util.exception.SysRuntimeException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

/**
 * <p>
 * Title: https的输出流</p>
 *
 * <p>
 * Description: </p>
 *
 * @author Updated by 思伟 on 2020/1/8
 */
public class HttpResponse {

    private HttpsURLConnection https;
    private HttpURLConnection http;
    private int status;
    private InputStream is;
    private String responseAsString = null;
    private boolean streamConsumed = false;

    public HttpResponse() {
    }

    public HttpResponse(HttpsURLConnection https) throws IOException {
        this.https = https;
        this.status = https.getResponseCode();
        if (null == (is = https.getErrorStream())) {
            is = https.getInputStream();
        }
    }

    public HttpResponse(HttpURLConnection http) throws IOException {
        this.http = http;
        this.status = http.getResponseCode();
        if (null == (is = http.getErrorStream())) {
            is = http.getInputStream();
        }
    }

    /**
     * 转换为输出流
     *
     * @return 输出流
     */
    public InputStream asStream() {
        if (streamConsumed) {
            throw new IllegalStateException("Stream has already been consumed.");
        }
        return is;
    }

    public String asString() throws SysRuntimeException {
        return asString("UTF-8");
    }

    /**
     * 将输出流转换为String字符串
     *
     * @return 输出内容
     * @throws SysRuntimeException
     */
    public String asString(String charset) throws SysRuntimeException {
        if (null == responseAsString) {
            BufferedReader br;
            try {
                try (InputStream stream = asStream()) {
                    if (null == stream) {
                        return null;
                    }
                    br = new BufferedReader(new InputStreamReader(stream, charset));
                    StringBuilder buf = new StringBuilder();
                    String line;
                    while (null != (line = br.readLine())) {
                        buf.append(line).append("\n");
                    }
                    this.responseAsString = buf.toString();
                }
                //输出流读取完毕，关闭连接
                if (https != null) {
                    https.disconnect();
                }
                //输出流读取完毕，关闭连接
                if (http != null) {
                    http.disconnect();
                }
                streamConsumed = true;
            } catch (NullPointerException npe) {
                // don't remember in which case npe can be thrown
                throw new SysRuntimeException(npe.getMessage(), npe);
            } catch (IOException ioe) {
                throw new SysRuntimeException(ioe.getMessage(), ioe);
            }
        }
        return responseAsString;
    }

    /**
     * 将输出流转换为JSON对象
     *
     * @return JSONObject对象
     */
    public JSONObject asJsonObject() throws SysRuntimeException {
        return JSONObject.parseObject(asString());
    }

    /**
     * 将输出流转换为JSON对象
     *
     * @return JSONArray对象
     */
    public JSONArray asJsonArray() throws SysRuntimeException {
        return JSONArray.parseArray(asString());
    }

    /**
     * 获取响应状态
     *
     * @return 响应状态
     */
    public int getStatus() {
        return status;
    }
}
