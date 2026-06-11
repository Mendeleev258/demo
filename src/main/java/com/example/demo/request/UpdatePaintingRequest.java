package com.example.demo.request;

import java.util.UUID;

public record UpdatePaintingRequest(String title, String style, Integer yearCreated, Integer version) {
}
