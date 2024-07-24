package com.zhou.s3.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
	
	@Autowired
    private AmazonS3 amazonS3;
 
    @Value("${aws.s3.bucketName}")
    private String bucketName;
 
    public void uploadFile(String key, MultipartFile file) throws IOException {
        var putObjectRequest = new PutObjectRequest(bucketName, key, file.getInputStream(), null);
        amazonS3.putObject(putObjectRequest);
    }
    
    public void deleteFile(String key) {
    	var deleteObjectRequest = new DeleteObjectRequest(bucketName, key);
    	amazonS3.deleteObject(deleteObjectRequest);
    }
    
    public byte[] getFile(String key) throws IOException {
    	var getObjectRequest = new GetObjectRequest(bucketName, key);
    	var file = amazonS3.getObject(getObjectRequest);
    	var in = file.getObjectContent();
    	return in.readAllBytes();
    }
}
