package com.example.demo.controller;

import com.example.demo.entity.Painting;
import com.example.demo.request.CreatePaintingRequest;
import com.example.demo.request.UpdatePaintingRequest;
import com.example.demo.service.PaintingService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/paintings")
public class PaintingController {
    private final PaintingService paintingService;

    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping
    public List<Painting> findAll(@RequestParam int page, @RequestParam int size) {
        return paintingService.findAll(page, size);
    }

    @GetMapping("/{id}")
    public Optional<Painting> findById(@PathVariable Integer id) {
        return paintingService.findById(id);
    }

    @PostMapping
    public Painting createPainting(@RequestBody CreatePaintingRequest request, Authentication auth) {
        return paintingService.create(request, auth);
    }

    @GetMapping("/search")
    public List<Painting> findByTitle(@RequestParam String title) {
        return paintingService.findByTitle(title);
    }

    @GetMapping("/search_by_user")
    public List<Painting> findByUserId(@RequestParam UUID userId) {
        return paintingService.findByUserId(userId);
    }

    @PutMapping("/{id}")
    public Painting updatePainting(@PathVariable Integer id,
                                   @RequestBody UpdatePaintingRequest request, Authentication auth) {
        return paintingService.update(id, request, auth);
    }

    @DeleteMapping("/{id}")
    public void deletePainting(@PathVariable Integer id, Authentication auth) {
        paintingService.deleteById(id, auth);
    }
}
