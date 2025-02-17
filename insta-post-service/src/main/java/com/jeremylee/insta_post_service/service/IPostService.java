package com.jeremylee.insta_post_service.service;

import com.jeremylee.insta_post_service.model.Post;
import com.jeremylee.insta_post_service.request.CreatePostRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface IPostService {

    Post getPostById(UUID id);

    List<Post> getAllPosts();

    Page<Post> getAllPosts(int page, int size);

    Post createPost(MultipartFile file, String content, String userId);

    Post updatePost(MultipartFile file, String content, UUID postId, String userId);

    void deletePost(UUID postId, String userId);
}
