package com.jeremylee.insta_media_service.adapter;

import java.io.File;

public interface ImageStorageAdapter {

    String uploadFile(File file, String contentType);

}
