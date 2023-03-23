package cn.xa87.member.controller;

import cn.xa87.common.utils.FileUploadUtils;
import cn.xa87.common.web.Response;
import cn.xa87.member.check.HeaderChecker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Api(value = "用户注册/登录/忘记密码/重置密码", tags = {"用户注册/登录/忘记密码/重置密码"})
@RestController
@RequestMapping("/upload")
@Slf4j
public class FileUploadController {

    @Value("${test.upload}")
    private String uploadPath;

    @ApiOperation(",实名认证")
    @PostMapping()
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response advancedCertification(MultipartFile positiveLink) {
//        String upload = "";
//        try{
//            upload = FileUploadUtils.upload(positiveLink);
//        }catch (Exception e){
//            log.info("上传图片出现错误 {}",e.getMessage());
//        }

        return Response.success(positiveLink.getOriginalFilename());
    }

    @ApiOperation("上传")
    @PostMapping("/new")
    @HeaderChecker(headerNames = {"token", "userId"})
    public Response uploadFile(MultipartFile file, HttpServletRequest req){
        if (file.isEmpty()) return Response.failure("上传失败，请选择文件！");
        Path path = Paths.get(uploadPath);
        if (Files.notExists(path) || !Files.isDirectory(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                return Response.failure(e.getMessage());
            }
        }
        String realPath = path.toAbsolutePath().toString() + File.separator;
        String format = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        File folder = new File(realPath + format);
        if (!folder.isDirectory()) folder.mkdirs();
        String oldName = file.getOriginalFilename();
        String suffix = oldName.substring(oldName.lastIndexOf("."));
        String newName = UUID.randomUUID().toString() + suffix;
        try {
            file.transferTo(new File(folder, newName));
        } catch (IOException e) {
            return Response.failure(e.getMessage());
        }
        String url = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/" + "upload" + "/" + format + newName;
//        String url = req.getScheme() + "://" + "192.168.4.15" + ":" + req.getServerPort() + "/" + AppConfig.getUpload() + "/" + format + newName;
        Map<String, Object> map = new HashMap<>();
        map.put("url", url);
        return Response.success(map);
    }
}
