package com.jeremiasAvero.app.image.application;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadProductImage(Long productId, MultipartFile file) throws IOException;

    default void validate(MultipartFile f){
        if(f==null || f.isEmpty()) throw new IllegalArgumentException("Empty File");
        if(f.getSize() > 10* 1024 * 1024) throw new IllegalArgumentException("Max 10MB");
        String ct = f.getContentType();
        if (ct == null || !(ct.equals("image/jpeg") || ct.equals("image/png") || ct.equals("image/webp")))
            throw new IllegalArgumentException("Only jpg/png/webp");
    }

}
