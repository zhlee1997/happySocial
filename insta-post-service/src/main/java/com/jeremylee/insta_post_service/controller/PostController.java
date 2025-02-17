package com.jeremylee.insta_post_service.controller;

import com.jeremylee.insta_post_service.exception.ResourceNotFoundException;
import com.jeremylee.insta_post_service.exception.UserIDNotMatchException;
import com.jeremylee.insta_post_service.model.Post;
import com.jeremylee.insta_post_service.response.ApiResponse;
import com.jeremylee.insta_post_service.service.IPostService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/post")
public class PostController {
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private IPostService iPostService;

    @GetMapping("/{postId}")
    public ResponseEntity<ApiResponse> getPostById(@PathVariable UUID postId) {
        try {
            Post post = iPostService.getPostById(postId);
            return ResponseEntity.ok(new ApiResponse("Success", post));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/posts")
    public ResponseEntity<ApiResponse> getPosts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        try {
            List<Post> posts;

            if (page == null || size == null) {
                posts = iPostService.getAllPosts();
            } else {
                Page<Post> paginatedPosts = iPostService.getAllPosts(page, size);
                return ResponseEntity.ok(new ApiResponse("Success", paginatedPosts));
            }
            return ResponseEntity.ok(new ApiResponse("Success", posts));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/debug-headers")
    public String debugHeaders(HttpServletRequest request) {
        logger.info("Received Headers from Gateway:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            logger.info("{}: {}", headerName, request.getHeader(headerName));
        }
        return "Check logs for headers";
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createPost(
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") String content,
            @RequestHeader Map<String, String> headers) {
        String userId = headers.get("userid"); // Extract userId from headers
        logger.info(userId);

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Creation failed: User ID required", null));
        }
        try {
            Post post = iPostService.createPost(file, content, userId);
            return ResponseEntity.ok(new ApiResponse("Success", post));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<ApiResponse> updatePost(
            @PathVariable UUID postId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("content") String content,
            @RequestHeader Map<String, String> headers) {
        String userId = headers.get("userid"); // Extract userId from headers
        logger.info(userId);

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Update Post failed: User ID required", null));
        }
        try {
            Post post = iPostService.updatePost(file, content, postId, userId);
            return ResponseEntity.ok(new ApiResponse("Success", post));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> deletePost(
            @PathVariable UUID postId,
            @RequestHeader Map<String, String> headers) {
        String userId = headers.get("userid"); // Extract userId from headers
        logger.info(userId);

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("Delete Post failed: User ID required", null));
        }
        try {
            iPostService.deletePost(postId, userId);
            return ResponseEntity.ok(new ApiResponse("Successfully deleted post", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
