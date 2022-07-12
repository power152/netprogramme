package net;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author Kiteye
 * @date 12/7/2022
 * @Description UDP 回显服务器
 */

public class UdpEchoServer {

      // 这里 创建了一个 socket 对象，绑定了本机中随机端口
    private DatagramSocket socket = null;

    // 这里是服务器上面的端口，由于ip没有指定，相当于0.0.0.0 服务器监听地址（绑定到当前主机上所有网卡上）
    public UdpEchoServer(int port) throws SocketException {
        // 切记这里的 端口，不能被别的进程绑定，一个进程对应一个端口，
        // 否则，抛出异常，构造socket 失败
        socket = new DatagramSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动！");
        // 服务器需要 7*24 开着
        while(true){
            /**
             * 这里面的操作设计三步：
             * 1、读取客户端发来的请求
             * 2、根据请求，计算出响应
             * 3、把响应返回给客服端
             */
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(requestPacket);
            // 这里需要把DatagramPacket 中的数据提取传来，转换成 String ，String 方便之后的代码处理
            String request = new String(requestPacket.getData(),0,requestPacket.getLength());
            // 2.
            // 因为这个代码是 echo server ，故客服端发什么就响应什么。
            String response = process(request);

            // 3.
            // 数据从哪里来就返回哪里去
            // 此处的 requestPacket.getSocketAddress 就描述了客户端的 IP 和端口号. 就直接原路返回即可
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),response.getBytes().length,
                    requestPacket.getSocketAddress());
            socket.send(responsePacket);
            System.out.printf("[%s:%d] req: %s; resp: %s\n", requestPacket.getAddress().toString(),
                    requestPacket.getPort(), request, response);
        }
    }

    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
        server.start();
    }
}
