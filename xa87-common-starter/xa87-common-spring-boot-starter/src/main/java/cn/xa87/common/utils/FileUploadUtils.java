package cn.xa87.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Slf4j
public class FileUploadUtils {

    public static String upload(MultipartFile file) throws Exception {
        String path = null;// 文件路径

        if (file != null) {// 判断上传的文件是否为空
            String type = null;// 文件类型
            String fileName = file.getOriginalFilename();// 文件原名称
            log.info("上传的文件原名称:" + fileName);

            // 判断文件类型
            type = fileName.indexOf(".") != -1 ? fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()) : null;
            if (type != null) {// 判断文件类型是否为空

                if ("GIF".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase()) || "JPG".equals(type.toUpperCase()) || "JPEG".equals(type.toUpperCase()) ) {

                    String uuid = UUID.randomUUID().toString().replaceAll("-","");
                    fileName = uuid + "."+type;
                    log.info("文件名称："+fileName);

                    // 添加日期
                    //String datePath = new DateTime().toString("yyyyMMdd");
                    path = "/home/upload/certification/";
                    File f = new File(path);
                    if (!f.exists()){
                        f.mkdirs();
                    }

                    String pathName = path + fileName;
                    log.info("存放图片文件的路径:" + pathName);

                    // 转存文件到指定的路径
                    file.transferTo(new File(pathName));

                    log.info("文件成功上传到指定目录下");
                    return fileName;
                }

            } else {
                return "不是我们想要的文件类型,请按要求重新上传";
            }
        } else {
            return "文件类型为空";
        }
        return "已经成功上传到指定目录";

    }
}
