package cl.usm.gestionPeliculasMemoria.repositories;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import jakarta.validation.constraints.Null;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PeliculasRepositoryImplTest {

    PeliculasRepositoryImpl peliculasRepository;

    @BeforeEach
    void setUp() {
        peliculasRepository = new PeliculasRepositoryImpl();
    }

    @Test
    void insertNull() {
        Pelicula peliculaTest= new Pelicula();
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(peliculaTest);
        });
        assertEquals("El ID de la pelicula no puede ser nulo", exception.getMessage());
    }
    @Test
    void insertDuplicatedID() {
        Pelicula peliculaTest1 = new Pelicula();
        peliculaTest1.setId("IDTEST");
        peliculasRepository.insert(peliculaTest1);
        Pelicula peliculaTest2 = new Pelicula();
        peliculaTest2.setId("IDTEST");
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            peliculasRepository.insert(peliculaTest2);
        });
        assertEquals("La pelicula con ID IDTEST ya existe", exception.getMessage());
    }
    @Test
    void insertOk() {
        Pelicula peliculaTest = new Pelicula();
        peliculaTest.setId("IDTEST");
        Pelicula peliculaResult = peliculasRepository.insert(peliculaTest);
        assertEquals( peliculaTest, peliculaResult);
    }

    @Test
    void findByIdNull() {
        Pelicula peliculaTest = peliculasRepository.findById(null);
        assertNull(peliculaTest);
    }
    @Test
    void findByIdNotFound() {
        Pelicula peliculaResult = peliculasRepository.findById("randomID");
        assertNull(peliculaResult);
    }
    @Test
    void findByIdOk() {
        Pelicula peliculaTest = new Pelicula("TestID", "Test Movie", "director", null, null);
        peliculasRepository.insert(peliculaTest);
        Pelicula peliculaResult = peliculasRepository.findById("TestID");
        assertEquals(peliculaTest,peliculaResult);
    }

}