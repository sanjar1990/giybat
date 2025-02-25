package api.giybat.uz.service;

import api.giybat.uz.dto.AttachDTO;
import api.giybat.uz.entity.AttachEntity;
import api.giybat.uz.exceptions.AppBadException;
import api.giybat.uz.exceptions.ItemNotFoundException;
import api.giybat.uz.repository.AttachRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

@Service
public class AttachService {
    @Autowired
    private AttachRepository attachRepository;
    @Value("${attach.folder.name}")
    private String folderName;
    @Value("${attach.open.url}")
    private String attachUrl;

    public AttachDTO upload(MultipartFile file) {
        if (file.isEmpty()) throw new AppBadException("File not found");
        try {
            String pathFolder = getYmdString();
            String key = UUID.randomUUID().toString();
            String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
            File folder = new File(folderName + "/" + pathFolder);
            if (!folder.exists()) {
                boolean t = folder.mkdirs();
            }
            byte[] bytes = file.getBytes();
            Path path = Paths.get(folderName + "/" + pathFolder + "/" + key + "." + extension);
            Files.write(path, bytes);
            AttachEntity entity = new AttachEntity();
            entity.setId(key + "." + extension);
            entity.setPath(pathFolder);
            entity.setExtension(extension);
            entity.setOrigenName(file.getOriginalFilename());
            entity.setSize(file.getSize());
            attachRepository.save(entity);
            return toDTO(entity);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setOrigenName(entity.getOrigenName());
        dto.setSize(entity.getSize());
        dto.setExtension(entity.getExtension());
        dto.setPath(entity.getPath());
        dto.setUrl(openUrl(entity.getId()));
        return dto;
    }
    public ResponseEntity<Resource>open(String id){
        AttachEntity entity=getEntity(id);
        Path filePath = Paths.get(getPath(entity)).normalize();
        Resource resource;
        try {
            resource=new UrlResource(filePath.toUri());
            if(!resource.exists())
                throw new ItemNotFoundException("File not found");
            String contentType=Files.probeContentType(filePath);
            if(contentType==null){
                contentType="application/octet-stream";
            }
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    private String getPath(AttachEntity entity){
        return folderName + "/" + entity.getPath()+"/"+entity.getId();
    }
    private AttachEntity getEntity(String id){
        return attachRepository.findById(id).orElseThrow(() -> new AppBadException("File not found"));
    }

    private String openUrl(String id) {
        return attachUrl +"/"+ id;
    }

    private String getYmdString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        return year + "/" + month + "/" + day;
    }

    private String getExtension(String filename) {
        int lastIndex = filename.lastIndexOf(".");
        return filename.substring(lastIndex + 1);
    }
}
