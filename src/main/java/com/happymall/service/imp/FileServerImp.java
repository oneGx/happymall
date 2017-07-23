package com.happymall.service.imp;
import com.google.common.collect.Lists;
import com.happymall.service.*;
import com.happymall.util.FTPUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by onegx on 17-7-20.
 */
@Service("iFileServer")
public class FileServerImp implements iFileServer {
    private Logger logger = LoggerFactory.getLogger(FileServerImp.class);

    @Override
    public String upLoad(MultipartFile file,String path){
        String fileName = file.getOriginalFilename();
        //扩展名
        //.jpg
        String fileExtensionName = fileName.substring(fileName.lastIndexOf(".")+1);
        String uploadFileName = UUID.randomUUID().toString()+"."+fileExtensionName;
        logger.info("开始上传文件,上传文件的文件名为:{},上传路径为:{},新文件名{}",fileName,path,uploadFileName);
        File fileDir = new File(path);

        if(!fileDir.exists()){
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(path,uploadFileName);
        try{
            file.transferTo(targetFile);
            //文件已经上传成功了
            FTPUtil.uploadFile(Lists.newArrayList(targetFile));
            targetFile.delete();
        }catch (IOException e){
            logger.error("上传文件异常",e);
            return null;
        }
        return targetFile.getName();
    }
}
