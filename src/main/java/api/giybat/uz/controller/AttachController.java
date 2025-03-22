package api.giybat.uz.controller;

import api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.service.AttachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/attach")
public class AttachController {
    @Autowired
    private AttachService attachService;

    @PostMapping("/upload")
    public ResponseEntity<AttachDTO>upload(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(attachService.upload(file));
    }
    @GetMapping("/open/{id}")
    public ResponseEntity<Resource>open(@PathVariable String id) {
        return attachService.open(id);
    }

}
