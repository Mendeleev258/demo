package com.example.demo.request;

import java.util.UUID;

public record CreatePaintingRequest(String title, String style, Integer yearCreated) {
}
