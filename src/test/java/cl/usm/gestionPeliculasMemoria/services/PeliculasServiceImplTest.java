package cl.usm.gestionPeliculasMemoria.services;

import cl.usm.gestionPeliculasMemoria.entities.Pelicula;
import cl.usm.gestionPeliculasMemoria.repositories.PeliculasRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PeliculasServiceImplTest {

    PeliculasServiceImpl peliculasService;
    @Mock
    PeliculasRepositoryImpl peliculasRepository;

    @BeforeEach
    void setUp() {
        peliculasService = new PeliculasServiceImpl(peliculasRepository);
    }

    @Test
    void createPeliculaOk() {
        Pelicula peliculaTest = new Pelicula();
        peliculaTest.setId("TestID");
        peliculaTest.setTitulo("Test Name");
        peliculaTest.setDirector("Test Director");

        when(peliculasRepository.insert(any(Pelicula.class))).thenReturn(peliculaTest);
        Pelicula peliculaResultado = peliculasService.createPelicula(peliculaTest);

        assertNotNull(peliculaResultado);
    }

    @Test
    void createPeliculaNok() {
        Pelicula peliculaTest = new Pelicula();

        when(peliculasRepository.insert(any(Pelicula.class))).thenThrow(new NumberFormatException());
        Pelicula peliculaResult = peliculasService.createPelicula(peliculaTest);

        assertNull(peliculaResult);
    }

    @Test
    void filterID() {
        Pelicula peliculaTest1 = new Pelicula("TestID1", "Test Movie", "director", null, null);
        Pelicula peliculaTest2 = new Pelicula("TestID2", "Test Movie", "director", null, null);

        when(peliculasRepository.findAll()).thenReturn(List.of(peliculaTest1, peliculaTest2));
        List<Pelicula> resultado = peliculasService.filter("TestID1");

        assertEquals(1, resultado.size());
        assertEquals("TestID1", resultado.get(0).getId());
    }
    @Test
    void filterTitulo() {
        Pelicula peliculaTest1 = new Pelicula("TestID1", "Movie Test", "director", null, null);
        Pelicula peliculaTest2 = new Pelicula("TestID2", "Test Movie", "director", null, null);
        Pelicula peliculaTest3 = new Pelicula("TestID3", "Test Movie", "director", null, null);

        when(peliculasRepository.findAll()).thenReturn(List.of(peliculaTest1, peliculaTest2, peliculaTest3));
        List<Pelicula> resultado = peliculasService.filter("Test Movie");

        assertEquals(2, resultado.size());
        assertEquals("Test Movie", resultado.get(0).getTitulo());
    }
    @Test
    void filterNone() {
        Pelicula peliculaTest1 = new Pelicula("TestID1", "Test Movie", "director", null, null);
        Pelicula peliculaTest2 = new Pelicula("TestID2", "Test Movie", "director", null, null);

        when(peliculasRepository.findAll()).thenReturn(List.of(peliculaTest1, peliculaTest2));
        List<Pelicula> resultado = peliculasService.filter("TestID3");

        assertEquals(0, resultado.size());
    }
}