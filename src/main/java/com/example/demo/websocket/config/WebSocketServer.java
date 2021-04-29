package com.example.demo.websocket.config;

import com.example.demo.business.util.StringUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
import java.util.concurrent.ConcurrentHashMap;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * @author zhengkai.blog.csdn.net
 */
@Slf4j
@ServerEndpoint("/websocket/{userId}")
@Component
@Data
public class WebSocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, Object> fileNameMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            webSocketMap.put(userId, this);
            //加入set中
            addOnlineCount();
            //在线数加1
        }

        log.info("用户连接:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("msg","连接成功");
            sendMessage(jsonObject.toJSONString());
        } catch (IOException e) {
            log.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        log.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.info("用户消息:" + userId + ",报文:" + message);
        //可以群发消息
        //消息保存到数据库、redis
        if (StringUtils.isNotBlank(message)) {
            try {
                //解析发送的报文
                JSONObject jsonObject = JSON.parseObject(message);
                //追加发送人(防止串改)
                jsonObject.put("fromUserId", this.userId);
                if (jsonObject.containsKey("isFile")){
                    fileNameMap.put(session.getId(),jsonObject);
                    return;
                }
                String toUserId = jsonObject.getString("toUserId");
                //传送给对应toUserId用户的websocket
                if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                    webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                } else {
                    log.error("请求的userId:" + toUserId + "不在该服务器上");
                    //否则不在这个服务器上，发送到mysql或者redis
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(byte[] data, Session session){
        System.out.println("收到二进制流:");
        // 告诉前台可以继续发送了.
        try {
            JSONObject jsonObject = (JSONObject) fileNameMap.get(session.getId());
//            // 将二进制流保存为文件, 文件名从连接对象中取出
//            saveFileFromBytes(data, "D:\\upFiles/" + jsonObject.getString("contentText"));
            String toUserId = jsonObject.getString("toUserId");
            //传送给对应toUserId用户的websocket
            if (StringUtils.isNotBlank(toUserId) && webSocketMap.containsKey(toUserId)) {
                webSocketMap.get(toUserId).sendMessage(jsonObject.toJSONString());
                synchronized (jsonObject){
                    webSocketMap.get(toUserId).sendMessage(data);
                }
            } else {
                log.error("请求的userId:" + toUserId + "不在该服务器上");
                //否则不在这个服务器上，发送到mysql或者redis
            }
        } catch (NotYetConnectedException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean saveFileFromBytes(byte[] b, String outputFile) {
        FileOutputStream fstream = null;
        File file = null;
        try {
            file = new File(outputFile);
            fstream = new FileOutputStream(file, true);
            fstream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fstream != null) {
                try {
                    fstream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return true;
    }

//    @OnMessage
//    public void onMessage(byte[] data, boolean last,Session session){
//        ByteArrayInputStream bis = new ByteArrayInputStream(data);
//        BufferedImage bImage2;
//        try {
//            bImage2 = ImageIO.read(bis);
//            ImageIO.write(bImage2, "jpg", new File("output.jpg") );
//        } catch (IOException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//
//        System.out.println("image created");
//    }


    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void sendMessage(byte[] data) throws IOException {
        System.err.println("data.length:" + data.length);
        ByteBuffer byteBuffer = ByteBuffer.allocate(data.length);
//        byteBuffer.clear();
//        byteBuffer.get(data, 0, data.length);
        byteBuffer.put(data);
        byteBuffer.flip();
        this.session.getBasicRemote().sendBinary(byteBuffer);
    }


    /**
     * 发送自定义消息
     */
    public static void sendInfo(String message, @PathParam("userId") String userId) throws IOException {
        log.info("发送消息到:" + userId + "，报文:" + message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户" + userId + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }


}
