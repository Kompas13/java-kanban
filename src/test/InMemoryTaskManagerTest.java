import manager.InMemoryTaskManager;
import manager.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    InMemoryTaskManager createTaskManager() {
        return (InMemoryTaskManager) Managers.getDefaultTaskManager();
    }



}