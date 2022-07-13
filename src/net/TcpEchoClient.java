package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Kiteye
 * @date 13/7/2022
 * @Description TCP 的客户端
 *  这个客户端做的工作和 UDP 版本的 非常相似 只不过多了个建立连接的过程.
 */

public class TcpEchoClient {
    // 客服端操作系统会自动分配ip地址and端口号
    private Socket socket = null;
    public TcpEchoClient(String serverIp, int serverPort) throws IOException {
        // 客服端在 new Socket的时候已经建立好连接了
        // 与服务器建立连接，首先肯定要知道服务器的ip地址和端口号
        socket  = new Socket("127.0.0.1",9090);
    }

    public void start() {
        System.out.println("客服端启动！");
        Scanner scan = new Scanner(System.in);
        try(InputStream inputStream = socket.getInputStream()) {
            try(OutputStream outputStream = socket.getOutputStream()) {
                Scanner respScan  = new Scanner(inputStream);
                while(true) {
                    /**
                     * 1、从控制台读取用户输入的数据
                     * 2、根据用户输入的数据，构造请求，发送给服务端
                     * 3、从服务器读取响应
                     * 4、把响应显示出来
                     */
                    System.out.print("-> ");
                    String request = scan.next();

                    // 2、
                    PrintWriter writer  = new PrintWriter(outputStream);
                    writer.println(request);
                    writer.flush();

                    //3、
                    String response = respScan.next();

                    // 4、
                    System.out.printf("req: %s, resp: %s\n", request, response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient client = new TcpEchoClient("127.0.0.1",9090);
        client.start();
    }
}
