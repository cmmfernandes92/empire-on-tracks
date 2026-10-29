package pt.ipp.isep.dei.repository;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RepositoriesTest {

    @Test
    void testGetInstance() {
        Repositories instance = Repositories.getInstance();
        assertNotNull(instance);
    }
}

