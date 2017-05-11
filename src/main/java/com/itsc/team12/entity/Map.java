package com.itsc.team12.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Created by Sam on 5/10/2017.
 */
@Document
public class Map {

    @Id
    private String id;

    private String version;
    private String fileName;
    private LocalDateTime created;

    public Map() {
    }

    public Map(String id, String version, String fileName, LocalDateTime created) {
        this.id = id;
        this.version = version;
        this.fileName = fileName;
        this.created = created;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}
