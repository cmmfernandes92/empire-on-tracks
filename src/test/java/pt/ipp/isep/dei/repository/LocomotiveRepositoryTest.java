package pt.ipp.isep.dei.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import pt.ipp.isep.dei.domain.Locomotive;
import pt.ipp.isep.dei.repository.LocomotiveRepository;

import static org.junit.jupiter.api.Assertions.*;

public class LocomotiveRepositoryTest {

    private LocomotiveRepository repository;

    @BeforeEach
    public void setUp() {
        repository = LocomotiveRepository.getInstance();  // Obtém a instância do repositório
    }

    @Test
    public void testGetInstance_ShouldReturnSameInstance() {
        LocomotiveRepository instance1 = LocomotiveRepository.getInstance();
        LocomotiveRepository instance2 = LocomotiveRepository.getInstance();
        assertSame(instance1, instance2, "Instances should be the same.");
    }

    @Test
    public void testGetAllTemplates_ShouldReturnDefaultTemplates() {
        // Verifica se o repositório contém os templates padrão de locomotivas
        var templates = repository.getAllTemplates();
        assertFalse(templates.isEmpty(), "Templates list should not be empty.");
        assertEquals(3, templates.size(), "There should be 3 templates.");
    }

    @Test
    public void testAddTemplate_ShouldAddNewTemplate() {
        // Testa a adição de um novo template
        Locomotive loco = new Locomotive("F400", "Steam", 85000.0, false, 2500, 0.6, 90.0, 1880, 12000.0, 6000.0);
        repository.addTemplate(loco);

        var templates = repository.getAllTemplates();
        assertTrue(templates.contains(loco), "The new locomotive should be added to the repository.");
    }

    @Test
    public void testUpdateTemplate_ShouldUpdateExistingTemplate() {
        // Testa a atualização de um template existente
        Locomotive updatedLoco = new Locomotive("S100", "Steam", 75000.0, false, 2100, 0.6, 90.0, 1850, 10000.0, 5000.0);
        repository.updateTemplate(0, updatedLoco);

        var templates = repository.getAllTemplates();
        assertEquals(updatedLoco, templates.get(0), "The template should be updated.");
    }

    @Test
    public void testRemoveTemplate_ShouldRemoveExistingTemplate() {
        // Testa a remoção de um template existente
        repository.removeTemplate(1);  // Remove o template no índice 1 (D200)
        var templates = repository.getAllTemplates();
        assertEquals(2, templates.size(), "There should be 2 templates left after removal.");
    }

    @Test
    public void testGetTemplate_ShouldReturnCorrectTemplate() {
        // Testa o método que retorna um template baseado no índice
        Locomotive template = repository.getTemplate(0);
        assertNotNull(template, "The template should not be null.");
        assertEquals("S100", template.getId(), "The template should have the correct ID.");
    }

    @Test
    public void testGetTemplate_ShouldReturnNullForInvalidIndex() {
        // Testa se o método retorna null para índice inválido
        Locomotive template = repository.getTemplate(100);
        assertNull(template, "The template should be null for invalid index.");
    }
}
