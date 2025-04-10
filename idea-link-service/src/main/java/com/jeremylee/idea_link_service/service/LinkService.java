package com.jeremylee.idea_link_service.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jeremylee.idea_link_service.exception.ResourceNotFoundException;
import com.jeremylee.idea_link_service.model.Link;
import com.jeremylee.idea_link_service.repository.LinkRepo;

@Service
public class LinkService {

    @Autowired
    private LinkRepo linkRepo;

    public List<Link> getAllLinks() {
        return linkRepo.findAll();
    }

    public Link getLinkById(String id) {
        return linkRepo.findById(id).orElse(null);
    }

    public Link createLink(Link link) {
        return linkRepo.save(link);
    }

    public Link updateLink(String id, Link link) {
        Link existing = linkRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Link not found"));

        link.setId(existing.getId());
        link.setCreatedAt(existing.getCreatedAt());
        return linkRepo.save(link);
    }

    public void deleteLink(String id) {
        linkRepo.deleteById(id);
    }

}
