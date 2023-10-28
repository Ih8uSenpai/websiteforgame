package com.example.websiteforgame.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;

import java.io.IOException;

@RestController
@RequestMapping
public class DownloadController {

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile() throws IOException {
        // Инициализация Firebase (если вы еще этого не сделали в другом месте вашего приложения)
        /*
        FileInputStream serviceAccount = new FileInputStream("path/to/your/serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .setStorageBucket("your-app-id.appspot.com")
            .build();

        FirebaseApp.initializeApp(options);
        */

        // Загрузка файла из Firebase Storage
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.get("dsf.txt");

        if (blob == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(blob.getContent());

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + blob.getName() + "\"")
                .body(resource);
    }
}
