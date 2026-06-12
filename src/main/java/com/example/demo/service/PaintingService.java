package com.example.demo.service;

import com.example.demo.entity.Painting;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.PaintingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreatePaintingRequest;
import com.example.demo.request.UpdatePaintingRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PaintingService {

    private final PaintingRepository paintingRepository;
    private UserRepository userRepository;

    public PaintingService(PaintingRepository paintingRepository, UserRepository userRepository) {
        this.paintingRepository = paintingRepository;
        this.userRepository = userRepository;
    }

    public List<Painting> findAll(int page, int size) {
        return paintingRepository.findAll(page, size);
    }

    public Optional<Painting> findById(Integer id) {
        return paintingRepository.findById(id);
    }

    public Painting create(CreatePaintingRequest request, Authentication auth) {
        User user = userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));

        if (request.yearCreated() <= 0 || request.yearCreated() > Year.now().getValue()) {
            throw new ValidationException("Invalid year");
        }

        Painting painting = new Painting(null, request.title(),
                request.style(), request.yearCreated(), 0, user.getId());
        if (!paintingRepository.create(painting)) {
            throw new ValidationException("Painting creation failed");
        }
        return painting;
    }

    public List<Painting> findByTitle(String title) {
        return paintingRepository.findByTitle(title);
    }

    public List<Painting> findByUserId(UUID userId) {
        return paintingRepository.findByUserId(userId);
    }

    @Transactional
    public Painting update(Integer id, UpdatePaintingRequest request, Authentication auth) {
        Optional<Painting> maybePainting = paintingRepository.findById(id);
        if (maybePainting.isEmpty()) {
            throw new ValidationException("Painting not found");
        }

        if (request.yearCreated() <= 0 || request.yearCreated() > Year.now().getValue()) {
            throw new ValidationException("Invalid year");
        }

        Painting oldPainting = maybePainting.get();

        User user = userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));

        if (!oldPainting.getUserId().equals(user.getId())) {
            throw new ValidationException("You are not owner of this painting");
        }

        Painting painting = new Painting(id, request.title(), request.style(),
                request.yearCreated(), request.version(), user.getId());

        if (!paintingRepository.update(painting)) {
            throw new ValidationException("Painting was modified by another user");
        }
        return painting;
    }

    public void deleteById(Integer id, Authentication auth) {
        Optional<Painting> maybePainting = paintingRepository.findById(id);

        if (maybePainting.isEmpty()) {
            throw new ValidationException("Painting not found");
        }

        Painting painting = maybePainting.get();

        User user = userRepository.findByLogin(auth.getName())
                .orElseThrow(() -> new ValidationException("User not found"));

        if (!painting.getUserId().equals(user.getId())) {
            throw new ValidationException("You are not owner of this painting");
        }
        if (!paintingRepository.deleteById(id)) {
            throw new ValidationException("Painting not found");
        }
    }
}
