package net;

import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;

/**
 * @author Kiteye
 * @date 12/7/2022
 * @Description 英汉词典
 */

public class UdpDictServer extends UdpEchoServer {
    private HashMap<String,String> map = new HashMap<>();

    public UdpDictServer(int port) throws SocketException {
        super(port);

        map.put("cat","猫");
        map.put("fuck","我艹");
        map.put("dog","狗");
        map.put("pig","猪");
    }

    @Override
    public String process(String request) {
        return map.getOrDefault(request,"没有找到翻译");

    }

    public static void main(String[] args) throws IOException {
        UdpDictServer dict = new UdpDictServer(9090);
        dict.start();

    }

}
