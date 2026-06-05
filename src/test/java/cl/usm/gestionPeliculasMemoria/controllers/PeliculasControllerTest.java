package cl.usm.gestionPeliculasMemoria.controllers;

import cl.usm.gestionPeliculasMemoria.entities.Comentario;
import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.services.PeliculasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasControllerTest {

    PeliculasController peliculasController;
    @Mock
    PeliculasService peliculasService;

    @BeforeEach
    void setUp() {
        peliculasController = new PeliculasController(peliculasService);
    }

    @Test
    void getAllOk() {
        Pelicula peliculaTest1 = new Pelicula("TestID1", "Test Movie1", "director", null, null);
        Pelicula peliculaTest2 = new Pelicula("TestID2", "Test Movie2", "director", null, null);

        when(peliculasService.getAll()).thenReturn(List.of(peliculaTest1, peliculaTest2));
        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllOkQ() {
        Pelicula peliculaTest2 = new Pelicula("TestID2", "Test Movie2", "director", null, null);

        when(peliculasService.filter("TestID2")).thenReturn(List.of(peliculaTest2));
        ResponseEntity<List<Pelicula>> response = peliculasController.getAll("TestID2");

        assertEquals(200, response.getStatusCode().value());
        assertEquals(1, response.getBody().size());
        assertEquals("TestID2", response.getBody().get(0).getId());
    }

    @Test
    void getAllNok() {
        when(peliculasService.getAll()).thenThrow(new RuntimeException("error"));

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll(null);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void getAllNokQ() {
        when(peliculasService.filter(anyString())).thenThrow(new RuntimeException("error"));

        ResponseEntity<List<Pelicula>> response = peliculasController.getAll("TestID");

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void createPeliculaOk() {
        Pelicula peliculaTest = new Pelicula("TestID", "Test Movie", "director", "tok3n12345", null);

        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(peliculaTest);
        ResponseEntity<?> response = peliculasController.createPelicula(peliculaTest);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("TestID", ((Pelicula) response.getBody()).getId());
    }

    @Test
    void createPeliculaNok() {
        Pelicula peliculaTest = new Pelicula("TestID", "Test Movie", "director", null, null);

        when(peliculasService.createPelicula(any(Pelicula.class))).thenReturn(null);
        ResponseEntity<?> response = peliculasController.createPelicula(peliculaTest);

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void findByIdOk() {
        Pelicula peliculaTest = new Pelicula("TestID", "Test Movie", "director", null, null);

        when(peliculasService.findById("TestID")).thenReturn(peliculaTest);
        ResponseEntity<Pelicula> response = peliculasController.findById("TestID");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("TestID", response.getBody().getId());
    }

    @Test
    void findByIdNotFound() {
        when(peliculasService.findById("NOPE")).thenReturn(null);
        ResponseEntity<Pelicula> response = peliculasController.findById("NOPE");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void findByIdException() {
        when(peliculasService.findById(anyString())).thenThrow(new RuntimeException("error"));
        ResponseEntity<Pelicula> response = peliculasController.findById("TestID");

        assertEquals(500, response.getStatusCode().value());
    }

    @Test
    void getComentariosOk() {
        Comentario[] comentarios = {
                new Comentario("usuario1", "muy buena"),
                new Comentario("usuario2", "excelente")
        };
        Pelicula peliculaTest = new Pelicula("TestID", "Test Movie", "director", null, comentarios);

        when(peliculasService.findById("TestID")).thenReturn(peliculaTest);
        ResponseEntity<?> response = peliculasController.getComentarios("TestID");

        assertEquals(200, response.getStatusCode().value());
        Comentario[] body = (Comentario[]) response.getBody();
        assertNotNull(body);
        assertEquals(2, body.length);
    }

    @Test
    void getComentariosNotFound() {
        when(peliculasService.findById("NOPE")).thenReturn(null);
        ResponseEntity<?> response = peliculasController.getComentarios("NOPE");

        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void getComentariosException() {
        when(peliculasService.findById(anyString())).thenThrow(new RuntimeException("error"));
        ResponseEntity<?> response = peliculasController.getComentarios("TestID");

        assertEquals(500, response.getStatusCode().value());
    }
}
