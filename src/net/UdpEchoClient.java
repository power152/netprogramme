package net;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

/**
 * @author Kiteye
 * @date 12/7/2022
 * @Description UDP 客服端
 */

public class UdpEchoClient {
    private DatagramSocket socket = null;
    private String serverIp;
    private int serverPort;

    public UdpEchoClient(String ip,int port) throws SocketException {
        this.serverIp = ip;
        this.serverPort = port;
        socket  = new DatagramSocket();
    }

    public void start() throws IOException {
        Scanner scan = new Scanner(System.in);
        while(true) {
            /**
             * 1、从控制台，读取用户输入的数据
             * 2、把数据构造层 UDP数据报，发送给服务器
             * 3、从服务器读取响应数据
             * 4、把响应数据进行解析，并显示
             */
            System.out.println("-> ");
            String request = scan.next();

            // 2、
            // 这里的ip地址需要用到 InetAddress 来封装一下
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length,
                    InetAddress.getByName(serverIp),serverPort);
            socket.send(requestPacket);

            // 3、
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);

            // 4、
            // 第三个参数 不能写成 requestPacket.getData().length ,这是把数据全读出来，
            // requestPacket.getLength() 是读有效数据
            String response = new String(responsePacket.getData(),0,requestPacket.getLength());
            System.out.printf("req: %s; resp: %s\n", request, response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient client = new UdpEchoClient("127.0.0.1",9090);
        client.start();
    }

}
