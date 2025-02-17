package com.jeremylee.insta_post_service.repository;

import com.jeremylee.insta_post_service.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepo extends PagingAndSortingRepository<Post, UUID>, JpaRepository<Post, UUID> {
}
