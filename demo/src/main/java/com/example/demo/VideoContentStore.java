package com.example.demo;

import org.springframework.content.commons.repository.ContentStore;
import org.springframework.content.rest.StoreRestResource;

@StoreRestResource(path="videoStreams")
public interface VideoContentStore extends ContentStore<Video, String> {
}