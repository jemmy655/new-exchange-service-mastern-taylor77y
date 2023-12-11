package cn.xa87.member.controller;

import cn.xa87.common.web.Response;
import cn.xa87.member.utils.GoogleAuthenticator;
import org.apache.commons.codec.binary.Base64;
import org.iherus.codegen.qrcode.QrcodeGenerator;
import org.iherus.codegen.qrcode.SimpleQrcodeGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/google/auth")
public class GooGleAuthController {

    /**
     * 生成 Google 密钥，两种方式任选一种
     */
    @GetMapping("/getSecret")
    public Response getSecret(String name) {
        String secret = GoogleAuthenticator.getSecretKey();
        String qrCodeText = GoogleAuthenticator.getQrCodeText(secret, name, "");
        BufferedImage image = new SimpleQrcodeGenerator().generate(qrCodeText).getImage();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "png", stream);
        } catch (IOException e) {
            return Response.failure("qrcode异常");
        }
        byte[] bytes = Base64.encodeBase64(stream.toByteArray());
        String base64 = new String(bytes);
        String qrcode = "data:image/png;base64," + base64;
        Map<String,Object> map = new HashMap<>();
        map.put("secret",secret);
        map.put("qrcode",qrcode);
        return Response.success(map);
    }

    /**
     * 生成二维码，APP直接扫描绑定，两种方式任选一种
     */
    @GetMapping("/getQrcode")
    public void getQrcode(String secret,String name, HttpServletResponse response) throws Exception {
        // 生成二维码内容
        String qrCodeText = GoogleAuthenticator.getQrCodeText(secret, name, "");
        // 生成二维码输出
        new SimpleQrcodeGenerator().generate(qrCodeText).toStream(response.getOutputStream());
    }

    /**
     * 获取code
     */
    @GetMapping("/getCode")
    public String getCode(String secretKey) {
        return GoogleAuthenticator.getCode(secretKey);
    }

    /**
     * 验证 code 是否正确,
     * 暂不处理业务
     */
    @GetMapping("/checkCode")
    public Response checkCode(String secret, String code) {
        boolean b = GoogleAuthenticator.checkCode(secret, Long.parseLong(code), System.currentTimeMillis());
        if (b) {
            return Response.success();
        }
        return Response.failure("validation failed");
    }

}
