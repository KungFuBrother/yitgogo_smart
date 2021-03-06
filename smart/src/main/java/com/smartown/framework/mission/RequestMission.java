package com.smartown.framework.mission;

import android.text.TextUtils;

import com.smartown.jni.YtBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import yitgogo.smart.tools.LogUtil;
import yitgogo.smart.tools.PackageTool;
import yitgogo.smart.tools.SignatureTool;

public class RequestMission extends Mission {

    HttpURLConnection httpURLConnection;

    Request request;
    RequestListener requestListener;

    public RequestMission(Request request, RequestListener requestListener) {
        this.request = request;
        this.requestListener = requestListener;
    }

    private void post() {
        if (isCanceled()) {
            return;
        }
        try {
            LogUtil.logInfo("Request", "url:" + request.getUrl());
            URL url = new URL(request.getUrl());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setUseCaches(false); // Post 请求不能使用缓存
            httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");//表单参数类型
            httpURLConnection.setRequestMethod("POST");// 设定请求的方法为"POST"，默认是GET
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
            httpURLConnection.setRequestProperty("version", PackageTool.getVersionName());
            httpURLConnection.setRequestProperty("token", YtBox.encode(SignatureTool.key, SignatureTool.getSignature() + System.currentTimeMillis()));
            if (!request.getRequestParams().isEmpty()) {
                StringBuffer stringBuffer = new StringBuffer();
                for (int i = 0; i < request.getRequestParams().size(); i++) {
                    if (i > 0) {
                        stringBuffer.append("&");
                    }
                    stringBuffer.append(request.getRequestParams().get(i).getKey());
                    stringBuffer.append("=");
                    stringBuffer.append(request.getRequestParams().get(i).getValue());
                }
                if (isCanceled()) {
                    return;
                }
                LogUtil.logInfo("Request", "parameters:" + stringBuffer);
                httpURLConnection.setFixedLengthStreamingMode(stringBuffer.toString().getBytes().length);//请求长度
                OutputStream outputStream = httpURLConnection.getOutputStream();// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，所以在开发中不调用上述的connect()也可以)。
                outputStream.write(stringBuffer.toString().getBytes());
                outputStream.flush();
                outputStream.close();
            }
            if (isCanceled()) {
                return;
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                if (isCanceled()) {
                    return;
                }
                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    requestListener.sendMessage(new RequestMessage(MissionListener.PROGRESS_SUCCESS, "PROGRESS_SUCCESS", stringBuilder.toString()));
                }
            } else {
                if (isCanceled()) {
                    return;
                }
                requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, "PROGRESS_FAILED" + " " + String.valueOf(responseCode)));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (isCanceled()) {
                return;
            }
            requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, e.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
            if (isCanceled()) {
                return;
            }
            requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, e.getMessage()));
        } finally {
            httpURLConnection.disconnect();
        }
    }

    private void get() {
        if (isCanceled()) {
            return;
        }
        try {
            LogUtil.logInfo("Request", "url:" + request.getUrl());
            StringBuilder paramStringBuilder = new StringBuilder();
            if (!request.getRequestParams().isEmpty()) {
                paramStringBuilder.append("?");
                for (int i = 0; i < request.getRequestParams().size(); i++) {
                    if (i > 0) {
                        paramStringBuilder.append("&");
                    }
                    paramStringBuilder.append(request.getRequestParams().get(i).getKey());
                    paramStringBuilder.append("=");
                    paramStringBuilder.append(request.getRequestParams().get(i).getValue());
                }
                LogUtil.logInfo("Request", "parameters:" + paramStringBuilder.toString());
            }
            if (isCanceled()) {
                return;
            }
            URL url = new URL(request.getUrl() + paramStringBuilder.toString());
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setUseCaches(false); // Post 请求不能使用缓存
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
            httpURLConnection.setRequestProperty("version", PackageTool.getVersionName());
            httpURLConnection.setRequestProperty("token", YtBox.encode(SignatureTool.key, SignatureTool.getSignature() + System.currentTimeMillis()));
            if (isCanceled()) {
                return;
            }
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder stringBuilder = new StringBuilder();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                inputStream.close();
                if (isCanceled()) {
                    return;
                }
                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    requestListener.sendMessage(new RequestMessage(MissionListener.PROGRESS_SUCCESS, "PROGRESS_SUCCESS", stringBuilder.toString()));
                }
            } else {
                if (isCanceled()) {
                    return;
                }
                requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, "PROGRESS_FAILED" + " " + String.valueOf(responseCode)));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            if (isCanceled()) {
                return;
            }
            requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, e.getMessage()));
        } catch (IOException e) {
            e.printStackTrace();
            if (isCanceled()) {
                return;
            }
            requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FAILED, e.getMessage()));
        } finally {
            httpURLConnection.disconnect();
        }
    }

    @Override
    public void start() {
        if (isCanceled()) {
            return;
        }
        requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_START, "PROGRESS_START"));
        if (request.getRequestType().equals(Request.REQUEST_TYPE_POST)) {
            post();
        } else {
            get();
        }
        if (isCanceled()) {
            return;
        }
        requestListener.sendMessage(new MissionMessage(MissionListener.PROGRESS_FINISH, "PROGRESS_FINISH"));
    }

}
