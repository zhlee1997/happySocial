package com.jeremylee.idea_link_service.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.jeremylee.idea_link_service.model.Link;

@Repository
public interface LinkRepo extends MongoRepository<Link, String> {
    // Custom query methods can be defined here if needed
    // For example, findByUrl(String url);

}
