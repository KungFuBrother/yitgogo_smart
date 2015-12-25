package yitgogo.smart.print;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Base64;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import yitgogo.smart.tools.Device;
import yitgogo.smart.tools.LogUtil;

public class PrintService extends Service {

    private boolean isDestroy = false;
    private Client serverClient;

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.logInfo("PrintService", "onBind");
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.logInfo("PrintService", "onCreate");
        // 启动服务时连接打印服务器；
        connectPrintServer();
    }

    @Override
    @Deprecated
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        LogUtil.logInfo("PrintService", "onStart");
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
        serverClient.stop();
        super.onDestroy();
        LogUtil.logInfo("PrintService", "onDestroy");
    }

    private void connectPrintServer() {
        serverClient = new Client("print.yitos.net", 9988,
                new ChannelInboundHandlerAdapter() {

                    @Override
                    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                        LogUtil.logInfo("Socket", "channelInactive");
                    }

                    @Override
                    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
                        LogUtil.logInfo("Socket", "channelReadComplete");
                    }

                    @Override
                    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
                        LogUtil.logInfo("Socket", "channelRegistered");
                    }

                    // 收到消息时调用此方法
                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                        LogUtil.logInfo("Socket", "channelRead");
                        // 如果收到打印消息时，没有在打印，则进行打印操作
                        String message = new String(Base64.decode((String) msg, Base64.DEFAULT), "UTF-8");
                        LogUtil.logInfo("Socket", message);
                        Intent intent = new Intent(PrintService.this, PrintDialogActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("message", message);
                        startActivity(intent);
                    }

                    // 成功连接到打印服务器，注册设备
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        LogUtil.logInfo("Socket", "channelActive");
                        String message = "MachineCode:" + Device.getDeviceCode();
                        LogUtil.logInfo("Socket", message);
                        ctx.writeAndFlush(new String(message.getBytes(), "UTF-8"));
                    }

                    // 建立连接时调用此方法
                    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
                        LogUtil.logInfo("Socket", "channelRegistered");
                    }

                    // 断开连接
                    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
                        super.channelUnregistered(ctx);
                        LogUtil.logInfo("Socket", "channelUnregistered");
                        // 如果服务没有停止，重连打印服务器
                        if (!isDestroy) {
                            connectPrintServer();
                        }
                    }

                });
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    serverClient.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
