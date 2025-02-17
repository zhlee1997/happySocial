package com.jeremylee.insta_post_service.service;

import com.jeremylee.insta_post_service.exception.ResourceNotFoundException;
import com.jeremylee.insta_post_service.exception.UserIDNotMatchException;
import com.jeremylee.insta_post_service.model.Post;
import com.jeremylee.insta_post_service.model.message.ImageUploadMessage;
import com.jeremylee.insta_post_service.producer.ImageUploadProducer;
import com.jeremylee.insta_post_service.repository.PostRepo;
import com.jeremylee.insta_post_service.request.CreatePostRequest;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {
    private final String bucketName = "insta-post-image-bucket"; // Minio bucket name

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private ImageUploadProducer imageUploadProducer;

    @Override
    @Cacheable(value = "posts", key = "#id") // Caches the post by ID
    public Post getPostById(UUID id) {
        Post post = postRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cannot find the post"));
        try {
            // Generate a presigned URL for the uploaded file
            String preSignedUrl = minioClient.getPresignedObjectUrl(
                    io.minio.GetPresignedObjectUrlArgs.builder()
                            .bucket(bucketName)
                            .object(post.getImageUrl())
                            .method(Method.GET) // Specify the HTTP method here
                            .build());
            post.setImageUrl(preSignedUrl);
            return post;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> getAllPosts() {
        List<Post> posts = postRepo.findAll();

        return posts.stream().map(post -> {
            try {
                // Generate a presigned URL for uploaded file
                String preSignedUrl = minioClient.getPresignedObjectUrl(
                        io.minio.GetPresignedObjectUrlArgs.builder()
                                .bucket(bucketName)
                                .object(post.getImageUrl())
                                .method(Method.GET)
                                .build()
                );
                post.setImageUrl(preSignedUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate pre-signed URL for post ID: " + post.getId(), e);
            }
            return post;
        }).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "posts") // Caches the result in Redis
    public Page<Post> getAllPosts(int page, int size) {
        Page<Post> posts = postRepo.findAll(PageRequest.of(page, size));

        List<Post> convertedPosts = posts.getContent().stream().map(post -> {
            try {
                // Generate a presigned URL for uploaded file
                String preSignedUrl = minioClient.getPresignedObjectUrl(
                        io.minio.GetPresignedObjectUrlArgs.builder()
                                .bucket(bucketName)
                                .object(post.getImageUrl())
                                .method(Method.GET)
                                .build()
                );
                post.setImageUrl(preSignedUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to generate pre-signed URL for post ID: " + post.getId(), e);
            }
            return post;
        }).collect(Collectors.toList());

        return new PageImpl<>(convertedPosts, posts.getPageable(), posts.getTotalElements());
    }

    @Override
    @CacheEvict(value = "posts", allEntries = true) // Clears the cache on create
    public Post createPost(MultipartFile file, String content, String userId) {
        try {
            // Check if the bucket exists, if not create it
//            if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
//                minioClient.makeBucket(MakeBucketArgs.builder()
//                        .bucket(bucketName)
//                        .build());
//            }

//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // Ensure uniqueness

            // Upload the file
//            minioClient.putObject(
//                    PutObjectArgs.builder()
//                            .bucket(bucketName)
//                            .object(fileName)
//                            .stream(file.getInputStream(), file.getSize(), -1)
//                            .contentType(file.getContentType())
//                            .build()
//            );

            // Save the file temporarily
            File tempFile = new File(System.getProperty("java.io.tmpdir"), file.getOriginalFilename());
            file.transferTo(tempFile);

            // Create message
            ImageUploadMessage message = new ImageUploadMessage(
                    file.getOriginalFilename(),
                    tempFile.getAbsolutePath(),
                    file.getContentType()
            );

            // Send to RabbitMQ
            imageUploadProducer.sendMessage(message);

            Post post = new Post();
            post.setContent(content);
//            post.setImageUrl(fileName);
            post.setImageUrl(file.getOriginalFilename());
            post.setUserId(userId);
            return postRepo.save(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "posts", allEntries = true) // Clears the cache on create
    public Post updatePost(MultipartFile file, String content, UUID postId, String userId) {
        try {
            Optional<Post> post = postRepo.findById(postId);
            if (post.isPresent()) {
                // only the similar userId can update the info
                if (post.get().getUserId().equals(userId)) {
                    // whether it is existing or new image file, uploads to Minio
                    String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename(); // Ensure uniqueness

                    // Upload the file
                    minioClient.putObject(
                            PutObjectArgs.builder()
                                    .bucket(bucketName)
                                    .object(fileName)
                                    .stream(file.getInputStream(), file.getSize(), -1)
                                    .contentType(file.getContentType())
                                    .build());
                    post.get().setImageUrl(fileName);
                    post.get().setContent(content);
                    return postRepo.save(post.get());
                } else {
                    throw new UserIDNotMatchException("Error: User ID does not match");
                }
            } else {
                throw new ResourceNotFoundException("Cannot find the post");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @CacheEvict(value = "posts", allEntries = true) // Clears the cache on create
    public void deletePost(UUID postId, String userId) {
        try {
            postRepo.findById(postId).ifPresentOrElse(post -> {
                if (post.getUserId().equals(userId)) {
                    postRepo.delete(post);
                } else {
                    throw new UserIDNotMatchException("Error: User ID does not match");
                }
            }, () ->  {
                throw new ResourceNotFoundException("Cannot find the post");
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
