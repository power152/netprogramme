package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * @author Kiteye
 * @date 13/7/2022
 * @Description Tcp 服务器连接
 */

public class TcpThreadEchoServer {
    private ServerSocket  serverSocket = null;

    // 此处是服务器要绑定的端口号
    public TcpThreadEchoServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    // TCP 是需要建立连接才能通信
    public void start() throws IOException {
        System.out.println("服务器启动！");
        while(true) {
            // 需要建立好连接，才能通信
            Socket clientSocket = serverSocket.accept();
            // 建立好了连接，需要和客户端进行通信
            // 需要改动这里，把每次建立好的连接，创建一个新的线程来处理
            Thread t = new Thread(()->{
                processConnection(clientSocket);
            });
            t.start();
        }
    }

    private void processConnection(Socket clientSocket) {
        // 客服端的ip ，和端口号
        System.out.printf("[%s:%d] 客户端建立连接!\n",clientSocket.getInetAddress().toString(),clientSocket.getPort());


        // 因为是面向字节流，所以要用InputStream和OutputStream
        // 需要和客户端进行通信. 发送接收数据.
        // 这里的代码就和前面学过的文件操作(字节流对象) 是一模一样的
        // 通过 socket 对象拿到 输入流 对象. 读这个输入流, 就相当于从网卡读取数据.
        // 通过 socket 对象拿到 输出流 对象. 写这个输出流, 就相当于往网卡写入数据.
        try(InputStream inputStream = clientSocket.getInputStream()) {
            try(OutputStream outputStream = clientSocket.getOutputStream()) {
                Scanner scan = new Scanner(inputStream);
                while(true) {
                    /**
                     * 1、读取请求并解析
                     * 2、根据请求计算响应
                     * 3、把响应写回到客服端
                     */
                    // 1、

                    if (!scan.hasNext()) {
                        System.out.printf("[%s:%d] 客户端断开连接!\n", clientSocket.getInetAddress().toString(), clientSocket.getPort());
                        break;
                    }
                    String request = scan.next();

                    // 2、
                    String response = process(request);

                    // 3、
                    //    可以使用 PrintWriter 来封装一下 OutputStream
                    //    这里的 PrintWriter 这个类已经用过很多次. HttpServletResponse.getWriter()
                    PrintWriter writer = new PrintWriter(outputStream);
                    // 这里面的数据只是写到了内核中的接受缓存区中，等全部写完了，一起响应给客服端
                    writer.println(response);
                    // 为了保证写入的数据能够及时返回给客户端, 手动加上一个刷新缓冲区的操作
                    writer.flush();

                    System.out.printf("[%s:%d] req: %s, resp: %s\n", clientSocket.getInetAddress().toString(),
                            clientSocket.getPort(), request, response);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close(); // 关闭资源可不能省！！！！！！
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpThreadEchoServer server = new TcpThreadEchoServer(9090);
        server.start();
    }
}
