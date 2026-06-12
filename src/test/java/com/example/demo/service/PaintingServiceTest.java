package com.example.demo.service;

import com.example.demo.entity.Painting;
import com.example.demo.entity.User;
import com.example.demo.exception.ValidationException;
import com.example.demo.repository.PaintingRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.CreatePaintingRequest;
import com.example.demo.request.UpdatePaintingRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaintingServiceTest {

    String login = "ann";

    @Mock
    private PaintingRepository paintingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaintingService paintingService;

    @Test
    void findById_shouldReturnPainting_whenExists() {

        Painting painting = mock(Painting.class);

        when(paintingRepository.findById(1)).thenReturn(Optional.of(painting));

        Optional<Painting> result = paintingService.findById(1);

        assertTrue(result.isPresent());
        assertEquals(painting, result.get());
    }

    @Test
    void findById_shouldReturnEmpty_whenNotExists() {

        when(paintingRepository.findById(1)).thenReturn(Optional.empty());

        Optional<Painting> result = paintingService.findById(1);

        assertTrue(result.isEmpty());
    }

    @Test
    void create_shouldThrow_whenUserNotFound() {

        CreatePaintingRequest request = new CreatePaintingRequest("Test", "Style", 2000);

        when(authentication.getName()).thenReturn(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> paintingService.create(request, authentication));
    }

    @Test
    void create_shouldCreatePainting_whenValid() {

        CreatePaintingRequest request = new CreatePaintingRequest("Test", "Style", 2000);

        User user = mock(User.class);
        UUID userId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(login);
        when(user.getId()).thenReturn(userId);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        when(paintingRepository.create(any(Painting.class))).thenReturn(true);

        Painting result = paintingService.create(request, authentication);

        assertEquals("Test", result.getTitle());
        assertEquals("Style", result.getStyle());
        assertEquals(2000, result.getYearCreated());

        verify(paintingRepository).create(any(Painting.class));
    }

    @Test
    void create_shouldThrow_whenInsertFails() {

        CreatePaintingRequest request = new CreatePaintingRequest("Test", "Style", 2000);

        User user = mock(User.class);

        when(authentication.getName()).thenReturn(login);

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        when(paintingRepository.create(any(Painting.class))).thenReturn(false);

        assertThrows(ValidationException.class,
                () -> paintingService.create(request, authentication));
    }

    @Test
    void delete_shouldCallRepository_whenValid() {

        Integer id = 1;

        Painting painting = mock(Painting.class);
        User user = mock(User.class);

        UUID userId = UUID.randomUUID();

        when(authentication.getName()).thenReturn(login);

        when(painting.getUserId()).thenReturn(userId);
        when(user.getId()).thenReturn(userId);

        when(paintingRepository.findById(id)).thenReturn(Optional.of(painting));

        when(userRepository.findByLogin(login)).thenReturn(Optional.of(user));

        when(paintingRepository.deleteById(id)).thenReturn(true);

        paintingService.deleteById(id, authentication);

        verify(paintingRepository).deleteById(id);
    }

    @Test
    void update_shouldThrow_whenPaintingNotFound() {

        Integer id = 1;

        UpdatePaintingRequest request =
                new UpdatePaintingRequest("Test", "Style", 2000, 0);

        when(paintingRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class,
                () -> paintingService.update(id, request, authentication));
    }

    @Test
    void findAll_shouldReturnList() {

        when(paintingRepository.findAll(0, 10)).thenReturn(List.of());

        List<Painting> result = paintingService.findAll(0, 10);

        assertTrue(result.isEmpty());
    }
}