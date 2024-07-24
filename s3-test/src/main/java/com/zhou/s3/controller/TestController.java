package com.zhou.s3.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zhou.s3.service.S3Service;

@RestController
public class TestController {
	@Autowired
    private S3Service s3Service;
 
    @PostMapping("/upload")
    public Boolean uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    	s3Service.uploadFile(file.getOriginalFilename(), file);
    	return true;
    }
    
    @DeleteMapping("/delete")
    public Boolean uploadFile(@RequestParam("filename") String filename) throws IOException {
    	s3Service.deleteFile(filename);
    	return true;
    }
    
    @GetMapping("/get")
    @ResponseBody
    public byte[] getFile(@RequestParam("filename") String filename) throws IOException {
    	return s3Service.getFile(filename);
    }
}
