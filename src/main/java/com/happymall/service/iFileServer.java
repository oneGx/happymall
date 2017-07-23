package com.happymall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Created by onegx on 17-7-20.
 */
public interface iFileServer {
    public String upLoad(MultipartFile file, String path);
}
