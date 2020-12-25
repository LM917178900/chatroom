package com.example.demo.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import static java.lang.System.out;

/**
 * 接收客户端发送的数据
 *
 */
public class MyTcp {

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

    void getServer(){
        try {

            // 开启本地的socket服务
            serverSocket=new ServerSocket(8998);        //实例化Socket对象
            out.println("服务器套接字已创建成功");

            while(true) {
                out.println("等待客户机的连接");

                // 监听然后获取传输的连接
                //
                remoteSocket=serverSocket.accept();           //accept()方法会返回一个和客户端Socket对象相连的Socket对象

                // 从连接中获取输入流数据，
                reader=new BufferedReader(new InputStreamReader(remoteSocket.getInputStream()));

                // 解析输入流数据，展示出来；
                getClientMessage();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    //读取客户端发送过来的信息
    private void getClientMessage() {
        try {

            // 遍历输出多行数据
            while(true) {
                //获得客户端信息
                // 读取客户端发送的该行数据
                out.println("客户机："+reader.readLine());
            }
        }catch(Exception e) {
            e.printStackTrace();
        }

        // 关闭连接
        try {
            if(reader!=null) {
                reader.close();
            }
            if(remoteSocket!=null) {
                remoteSocket.close();
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // 新建tcp 服务端；
        MyTcp tcp=new  MyTcp();

        // 获取客户端连接和数据
        tcp.getServer();
    }

}
