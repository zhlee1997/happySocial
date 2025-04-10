package com.jeremylee.idea_link_service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jeremylee.idea_link_service.model.Link;
import com.jeremylee.idea_link_service.service.LinkService;

@RestController
@RequestMapping("/api/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @GetMapping
    public ResponseEntity<List<Link>> getAllLinks() {
        return ResponseEntity.ok(linkService.getAllLinks());
    }

    @GetMapping("/{id}")
    public Link getUserById(@PathVariable String id) {
        return linkService.getLinkById(id);
    }

    @PostMapping
    public Link createUser(@RequestBody Link link) {
        return linkService.createLink(link);
    }

    @PutMapping("/{id}")
    public Link updateUser(@PathVariable String id, @RequestBody Link link) {
        return linkService.updateLink(id, link);
    }

    @DeleteMapping("/{id}")
    public void deleteLink(@PathVariable String id) {
        linkService.deleteLink(id);
    }
}
