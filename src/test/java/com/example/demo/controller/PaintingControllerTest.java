package com.example.demo.controller;

import com.example.demo.entity.Painting;
import com.example.demo.request.CreatePaintingRequest;
import com.example.demo.request.UpdatePaintingRequest;
import com.example.demo.service.PaintingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaintingControllerTest {

    @Mock
    private PaintingService paintingService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaintingController paintingController;

    private final Integer id = 1;
    private final UUID userId = UUID.randomUUID();
    private final String title = "Mona Lisa";

    @Test
    void findAll_shouldReturnPaintings() {

        List<Painting> expected = List.of(new Painting(id, title, "style", 2000, 0, userId));

        when(paintingService.findAll(0, 10)).thenReturn(expected);

        List<Painting> result = paintingController.findAll(0, 10);

        assertThat(result).isEqualTo(expected);
        verify(paintingService).findAll(0, 10);
    }

    @Test
    void findById_shouldReturnPainting() {

        Painting painting = new Painting(id, title, "style", 2000, 0, userId);

        when(paintingService.findById(id)).thenReturn(Optional.of(painting));

        Optional<Painting> result = paintingController.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo(title);

        verify(paintingService).findById(id);
    }

    @Test
    void createPainting_shouldCallService() {

        CreatePaintingRequest request = new CreatePaintingRequest(title, "style", 2000);

        Painting painting = new Painting(id, title, "style", 2000, 0, userId);

        when(paintingService.create(request, authentication)).thenReturn(painting);

        Painting result = paintingController.createPainting(request, authentication);

        assertThat(result).isEqualTo(painting);

        verify(paintingService).create(request, authentication);
    }

    @Test
    void findByTitle_shouldReturnPaintings() {

        List<Painting> expected = List.of(new Painting(id, title, "style", 2000, 0, userId));

        when(paintingService.findByTitle(title)).thenReturn(expected);

        List<Painting> result = paintingController.findByTitle(title);

        assertThat(result).isEqualTo(expected);
        verify(paintingService).findByTitle(title);
    }

    @Test
    void findByUserId_shouldReturnPaintings() {

        List<Painting> expected = List.of(new Painting(id, title, "style", 2000, 0, userId));

        when(paintingService.findByUserId(userId)).thenReturn(expected);

        List<Painting> result = paintingController.findByUserId(userId);

        assertThat(result).isEqualTo(expected);
        verify(paintingService).findByUserId(userId);
    }

    @Test
    void updatePainting_shouldCallService() {

        UpdatePaintingRequest request = new UpdatePaintingRequest(title, "style", 2000, 0);

        Painting painting =  new Painting(id, title, "style", 2000, 0, userId);

        when(paintingService.update(id, request, authentication)).thenReturn(painting);

        Painting result = paintingController.updatePainting(id, request, authentication);

        assertThat(result).isEqualTo(painting);

        verify(paintingService).update(id, request, authentication);
    }

    @Test
    void deletePainting_shouldCallService() {

        paintingController.deletePainting(id, authentication);

        verify(paintingService).deleteById(id, authentication);
    }
}