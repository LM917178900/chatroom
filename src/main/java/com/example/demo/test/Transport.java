package com.example.demo.test;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.out;

/**
 * 发送消息
 * 1. 创建iframe,
 * 2. 创建socket
 * 3. 创建消息
 * 4. 发送消息
 * 5. 展示消息
 * <p>
 * 接收消息
 * 1. 创建iframe
 * 2. 获取socket
 * 3. 获取消息
 * 4. 展示消息
 *
 * @description: Transport
 * @author: leiming5
 * @date: 2020-12-21 13:22
 */
public class Transport extends CommonFrame {

    /**
     * 读取客户端传输的消息输出流
     */
    private BufferedReader reader;


    /**
     * 服务端socket
     */
    private ServerSocket serverSocket;

    /**
     * 客户端消息
     */
    private Socket remoteSocket;

    public Transport(String title) {
        super(title);
    }


    /**
     * 1. 将获取到的数据展示在文本域中；
     * 2. 获取输入信息，
     * 3. 获取需要传输的数据
     * 4. 将消息发送回去
     */
    void getServer() {
        try {

            // 开启本地的socket服务
            serverSocket = new ServerSocket(8998);        //实例化Socket对象
            out.println("服务器套接字已创建成功");

            while (true) {
                out.println("等待客户机的连接");
                // 监听然后获取传输的连接
                chatArea.append("等待客户机的连接\n");

                // 阻塞
                // 等待连接
                remoteSocket = serverSocket.accept();           //accept()方法会返回一个和客户端Socket对象相连的Socket对象

                // 从输入流中获取数据，
                reader = new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));

                // 获取远程的socket;
                socket = remoteSocket;

                // 将数据写入输出流
//                writer=new PrintWriter(remoteSocket.getOutputStream(),true);
                // 解析输入流数据，展示出来；
                getClientMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    //读取客户端发送过来的信息
    private void getClientMessage() {
        try {

            // 遍历输出多行数据
            while (true) {

                // reader 会阻塞，会一直等待消息
                //获得客户端信息
                String content = reader.readLine();
                // 读取客户端发送的该行数据
                out.println("客户机：" + content);

                // 消息在聊天区域展示，换行展示
                chatArea.append(content+"\n");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 关闭连接
        try {
            if (reader != null) {
                reader.close();
            }
            if (remoteSocket != null) {
                remoteSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // 新建tcp 服务端；
        Transport transport = new Transport("window 2");

        transport.setSize(400,400);
        // 客户端可见度
        transport.setVisible(true);

        // 获取客户端连接和数据
        transport.getServer();
    }
}
