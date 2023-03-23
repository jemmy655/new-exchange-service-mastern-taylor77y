package cn.xa87.socket.test;

import cn.xa87.socket.Xa87SocketServiceApplication;
import cn.xa87.socket.util.ZipUtil;
import org.springframework.boot.SpringApplication;

import java.io.IOException;

public class test {

    public static void main(String[] args) throws IOException {
        String data=
                "SDRzSUFBQUFBQUFBQUt0V3lpOUl6U3N0VUxLcXJ0VlJ5azBzU2M3d3lTd3VVYktLanRVQlM2WGtsK2VCSkdzQjBUNjZTQ29BQUFBPQ==" ;
        String ores=ZipUtil.gzip(data);
        String res=ZipUtil.gunzip(data);
        System.out.println(res);
        System.out.println(ores);
    }
}
