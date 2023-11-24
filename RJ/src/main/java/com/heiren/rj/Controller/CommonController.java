package com.heiren.rj.Controller;

import com.heiren.rj.Common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${RJ.path}")
    String basePath;
    /**
     * 文件上传
     * @param file
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        //file是一个临时文件 需要转存到指定的位置，否则会被删除

        //原始文件名字
        String originalFilename = file.getOriginalFilename();
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成文件名避免重复
        String fileName = UUID.randomUUID().toString()+suffix;
        //创建一个目录对象
        File file1 = new File(basePath);
        if (!file1.isDirectory()) {
            file1.mkdirs();
        }
        //将临时文件转到指定目录
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件的下载
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) {
        try {
            //创建输入流，来读取
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));
            //创建输出流，将文件写回到browser来展示
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");

            int len = 0;
            byte[] bytes = new byte[1024];
            while((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
            //关闭资源
            fileInputStream.close();
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
