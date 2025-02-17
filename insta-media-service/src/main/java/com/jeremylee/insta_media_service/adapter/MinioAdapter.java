package com.jeremylee.insta_media_service.adapter;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class MinioAdapter implements ImageStorageAdapter {
    private final String bucketName = "insta-post-image-bucket";

    @Autowired
    private MinioClient minioClient;

//    public MinioAdapter() {
//        this.minioClient = MinioClient.builder()
//                .endpoint("http://localhost:9002")
//                .credentials("ROOTNAME", "CHANGEME123")
//                .build();
//    }

    @Override
    public String uploadFile(File file, String contentType) {
        try (FileInputStream fis = new FileInputStream(file)) {
            // Check if the bucket exists, if not create it
            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());
            }

//            String fileName = UUID.randomUUID().toString() + "_" + file.getName();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(file.getName())
//                            .object(fileName)
                            .stream(fis, file.length(), -1)
                            .contentType(contentType)
                            .build()
            );
            return file.getName(); // Return file key for retrieval
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServerException e) {
            throw new RuntimeException(e);
        } catch (InsufficientDataException e) {
            throw new RuntimeException(e);
        } catch (ErrorResponseException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (InvalidResponseException e) {
            throw new RuntimeException(e);
        } catch (XmlParserException e) {
            throw new RuntimeException(e);
        } catch (InternalException e) {
            throw new RuntimeException(e);
        }
    }
}
