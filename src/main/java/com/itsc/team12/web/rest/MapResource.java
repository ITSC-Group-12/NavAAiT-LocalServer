package com.itsc.team12.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Sam on 5/10/2017.
 */
@RestController
@RequestMapping("/api")
public class MapResource {

    public ResponseEntity<?> checkVersion(){
        return null;
    }

    public ResponseEntity<?> update(){
        return null;
    }

    public ResponseEntity<?> upload(){
        return null;
    }
}
