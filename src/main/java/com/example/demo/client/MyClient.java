package com.example.demo.client;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class MyClient extends JFrame {

    // 将内存中的数据写入某个外部文件，
    private PrintWriter writer;
    Socket socket;
    private JTextArea chatArea = new JTextArea();
    private JTextField inputFrame = new JTextField();
    Container container;

    // 客户端构造方法
    public MyClient(String title) {

        // 默认通过标题构建客户端
        super(title);

        // 设置默认操作：关闭jframe 就退出
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 获取jframe 容器
        container = this.getContentPane();

        // 新建滚动文本框
        final JScrollPane scrollPane = new JScrollPane();
        // 设置文本框边界
        scrollPane.setBorder(new BevelBorder(BevelBorder.RAISED));
        // 设置文本域，剧中布局
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        // 设置
        scrollPane.setViewportView(chatArea);

        // 给容器添加输入框，并设置输入框位置
        container.add(inputFrame, "South");

        // 聚焦，并监听输入内容，输入框设置默认提示语句；
        inputFrame.addFocusListener(new JTextFieldListener(inputFrame, "请在此输入内容"));

        // 监听输入的内容，(回车，获取、发送、展示、清空内容)
        inputFrame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (inputFrame.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(MyClient.this, "请输入内容！");

                } else {

                    // 把监听到的消息，写入输出流，
                    writer.println(inputFrame.getText());

                    // 消息在聊天区域展示，换行展示
                    chatArea.append(inputFrame.getText() + "\n");
                    // 刷新
                    chatArea.setSelectionEnd(chatArea.getText().length());

                    // 清空输入框
                    inputFrame.setText("");
                }


            }
        });
    }

    /**
     * 焦点监听器
     * 通过聚焦和失去焦点的形式获取输入内容
     */
    class JTextFieldListener implements FocusListener {

        // 提示信息
        private String hintText;          //提示文字
        // 输入的内容
        private JTextField textField;

        // 设置监听器构造方法
        // 设置文本域和默认提示语
        public JTextFieldListener(JTextField textField, String hintText) {
            this.textField = textField;
            this.hintText = hintText;

            // 设置文本域默认提示语
            textField.setText(hintText);   // 默认直接显示
            // 设置文本域颜色
            textField.setForeground(Color.GRAY);
        }

        // 聚焦，清空提示语，
        @Override
        public void focusGained(FocusEvent e) {

            //获取焦点时，清空提示内容
            String temp = textField.getText();
            if (temp.equals(hintText)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }

        }

        // 失去焦点，获取文本，设置提示语
        @Override
        public void focusLost(FocusEvent e) {

            //失去焦点时，没有输入内容，显示提示内容
            String temp = textField.getText();
            if (temp.equals("")) {
                textField.setForeground(Color.GRAY);
                textField.setText(hintText);
            }

        }

    }

    /**
     * 设置连接
     * 首次，需要开启连接，发送消息就不需要再开启了
     */
    private void connect() {
        // 设置开启连接提示语句
        chatArea.append("尝试连接\n");
        try {
            // 开启socket,指定的地址和端口号
            socket = new Socket("127.0.0.1", 8998);

            // 把输出流的消息，写入socket中的outputStream,然后自动刷新
            writer = new PrintWriter(socket.getOutputStream(), true);

            // 显示连接成功
            chatArea.append("完成连接\n");

//            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//            while (true) {
//                String content = reader.readLine();
//                System.out.println("===========>" + content);
//                chatArea.append(content+"\n");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        //EventQueue事件队列，封装了异步事件指派机制
        EventQueue.invokeLater(() -> {

            // 开启客户端
            MyClient client = new MyClient("leiming5 chat room");

            // 设置客户端样式
            // 客户端高度和宽度
            client.setSize(400, 400);
            // 客户端可见度
            client.setVisible(true);

            // 开启客户端连接
            client.connect();

        });

    }

}
