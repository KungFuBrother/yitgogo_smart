package smartown.controller.mission;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yitgogo.smart.tools.LogUtil;
import yitgogo.smart.tools.PackageTool;

public class MissionController {

    static ExecutorService executorService = Executors.newFixedThreadPool(5);
    static Map<Context, List<Mission>> missions = Collections.synchronizedMap(new WeakHashMap<Context, List<Mission>>());

    /**
     * 启动网络任务
     *
     * @param context
     * @param request
     * @param requestListener
     */
    public static void startRequestMission(Context context, Request request, RequestListener requestListener) {
        RequestMission requestMission = new RequestMission(request, requestListener);
        executorService.submit(requestMission);
        syncMissions(context, requestMission);
    }

    public static void startControllableMission(Context context, ControllableMission controllableMission) {
        executorService.submit(controllableMission);
        syncMissions(context, controllableMission);
    }

    public static void cancelMissions(Context context) {
        List<Mission> contextMissions = missions.get(context);
        if (contextMissions != null) {
            for (Mission mission : contextMissions) {
                if (mission != null) {
                    mission.cancel();
                }
            }
        }
        missions.remove(context);
    }

    public static void syncMissions(Context context, Mission mission) {
        List<Mission> contextMissions;
        synchronized (missions) {
            contextMissions = missions.get(context);
            if (contextMissions == null) {
                contextMissions = Collections.synchronizedList(new LinkedList<Mission>());
                missions.put(context, contextMissions);
            }
        }
        contextMissions.add(mission);
    }

    public static String request(Request request) {
        try {
            LogUtil.logInfo("Request", "url:" + request.getUrl());
            URL url = new URL(request.getUrl());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoOutput(true);// 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在http正文内，因此需要设为true, 默认情况下是false;
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setUseCaches(false); // Post 请求不能使用缓存
            httpURLConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");//表单参数类型
            httpURLConnection.setRequestMethod("POST");// 设定请求的方法为"POST"，默认是GET
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
            httpURLConnection.setRequestProperty("version", PackageTool.getVersionName());
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
                LogUtil.logInfo("Request", "parameters:" + stringBuffer);
                httpURLConnection.setFixedLengthStreamingMode(stringBuffer.toString().getBytes().length);//请求长度
                OutputStream outputStream = httpURLConnection.getOutputStream();// 此处getOutputStream会隐含的进行connect(即：如同调用上面的connect()方法，所以在开发中不调用上述的connect()也可以)。
                outputStream.write(stringBuffer.toString().getBytes());
                outputStream.flush();
                outputStream.close();
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
                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    LogUtil.logInfo("Request", "result:" + stringBuilder.toString());
                    return stringBuilder.toString();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String get(Request request) {
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
            URL url = new URL(request.getUrl() + paramStringBuilder.toString());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setUseCaches(false); // Post 请求不能使用缓存
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
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
                if (!TextUtils.isEmpty(stringBuilder.toString())) {
                    LogUtil.logInfo("Request", "result:" + stringBuilder.toString());
                    return stringBuilder.toString();
                }
            } else {
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean download(String urlString, File file) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);// 设置是否从httpUrlConnection读入，默认情况下是true;
            httpURLConnection.setUseCaches(false); // Post 请求不能使用缓存
            httpURLConnection.setConnectTimeout(5000);//连接超时 单位毫秒
            httpURLConnection.setReadTimeout(5000);//读取超时 单位毫秒
            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream inputStream = httpURLConnection.getInputStream();
                if (inputStream != null) {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] b = new byte[512];
                    int readCount;
                    while ((readCount = inputStream.read(b)) != -1) {
                        fileOutputStream.write(b, 0, readCount);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    httpURLConnection.disconnect();
                    return true;
                }
            }
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
