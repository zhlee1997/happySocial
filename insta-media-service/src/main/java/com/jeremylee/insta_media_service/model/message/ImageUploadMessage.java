package com.jeremylee.insta_media_service.model.message;

import lombok.Data;


@Data
public class ImageUploadMessage {
    private static final long serialVersionUID = 1L;  // Recommended for Serializable classes
    private String fileName;
    private String filePath;
    private String contentType;
}
