package com.example.demo.entity;

import java.util.UUID;

public class Painting {
    private Integer id;
    private String title;
    private String style;
    private Integer yearCreated;
    private Integer version;
    private UUID userId;

    public Painting(Integer id,
                    String title,
                    String style,
                    Integer yearCreated,
                    Integer version,
                    UUID userId) {
        this.id = id;
        this.title = title;
        this.style = style;
        this.yearCreated = yearCreated;
        this.version = version;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getYearCreated() {
        return yearCreated;
    }

    public void setYearCreated(Integer yearCreated) {
        this.yearCreated = yearCreated;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
