package manager.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import manager.Managers;

class ManagersTest {
    @Test
    void shouldGetDefaultManager() {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    void shouldGetInMemoryTaskManager() {
        assertNotNull(Managers.getInMemoryTaskManager(Managers.getDefaultHistory()));
    }
}