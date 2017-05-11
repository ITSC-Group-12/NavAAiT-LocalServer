package com.itsc.team12.web.rest;

import com.itsc.team12.entity.Map;
import com.itsc.team12.repository.MapRepository;
import com.itsc.team12.web.rest.util.HeaderUtil;
import com.itsc.team12.web.rest.util.PlatformChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Created by Sam on 5/10/2017.
 */
@RestController
@RequestMapping("/api/map")
public class MapResource {

    @Autowired
    private MapRepository mapRepository;

    @Value("${root.directory}")
    private String rootDirectory;

    @RequestMapping(value = "/checkVersion", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> checkVersion() {

        if (mapRepository.count() > 0) {

            return new ResponseEntity<Object>(mapRepository.findTop1ByOrderByCreatedDesc(), HttpStatus.FOUND);
        } else {
            String empty = "Nothing to see here";
            return new ResponseEntity<Object>(empty, HttpStatus.NOT_FOUND);
        }
    }

    //  TODO Testing needed
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> update(HttpServletResponse response) {
        if (mapRepository.count() > 0) {

            Map result = mapRepository.findTop1ByOrderByCreatedDesc();
            File file = new File(result.getFileName());
            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource resource = null;
            try {
                resource = new ByteArrayResource(Files.readAllBytes(path));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return ResponseEntity.ok()
                    .contentLength(file.length())
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(resource);

        } else {
            String empty = "Nothing to see here";
            return new ResponseEntity<Object>(empty, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public ResponseEntity<?> upload(@RequestParam(value = "version") String version, @RequestParam("mapFile") MultipartFile mapFile) {
        if (version == null || version.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Map", "noversion", "No Map version provided")).body(null);
        }
        String rootPath;
        try {
            if (mapFile.isEmpty()) {
                return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Map", "nomapfile", "No Map File provided")).body(null);
            }
            rootPath = rootDirectory + PlatformChecker.FILE_SEPARATOR + "Uploads";
            File dir = new File(rootPath);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            byte[] bytes = mapFile.getBytes();
            File serverFile = new File(dir.getAbsoluteFile(), mapFile.getOriginalFilename());
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));
            outputStream.write(bytes);
            outputStream.close();
        } catch (IOException e) {
            String empty = "IO Error. Please inform admins";
            return new ResponseEntity<Object>(empty, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Map map = new Map();
        map.setVersion(version);
        map.setCreated(LocalDateTime.now());
        map.setFileName(rootPath + PlatformChecker.FILE_SEPARATOR + mapFile.getOriginalFilename());
        Map result = mapRepository.save(map);
        return ResponseEntity.accepted()
                .headers(HeaderUtil.createEntityCreationAlert("Map", result.getId().toString()))
                .body(result);
    }
}
