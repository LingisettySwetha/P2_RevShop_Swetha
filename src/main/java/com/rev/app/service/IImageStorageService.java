package com.rev.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface IImageStorageService {

    String storeProductImage(MultipartFile file);
}
