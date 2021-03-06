package pl.maltoza.tasks.boundary;

import lombok.Getter;
import org.springframework.stereotype.Repository;
import pl.maltoza.exceptions.NotFoundException;
import pl.maltoza.tasks.entity.Task;

import java.time.LocalDate;
import java.util.*;

@Getter
@Repository
public class MemoryTasksRepository implements TasksRepository {
    private final Set<Task> tasks = new HashSet<>();

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public List<Task> fetchAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public void addAll(Iterable<Task> taskIterable) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Task fetchById(Long id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found : " + id));

    }

    @Override
    public void deleteById(Long id) {
        findById(id)
                .ifPresentOrElse(tasks::remove, () -> {
                    throw new NotFoundException("Task not found  : " + id);
                });
    }

    @Override
    public void update(Long id, String title, String description) {
        Task task = findById(id)
                .orElseThrow(() -> new NotFoundException("Task not found : " + id));
        task.setTitle(title);
        task.setDescription(description);
    }

    @Override
    public void save(Task task) {
        tasks.add(task);
    }

    @Override
    public List<Task> findByTitle(String title) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Task> findWithAttachments() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Task> findPriorityTasks() {
        return null;
    }

    @Override
    public List<Task> findDueTasks(LocalDate now) {
        return null;
    }


    private Optional<Task> findById(Long id) {
        return tasks.stream()
                .filter(task -> task.getId().equals(id))
                .findFirst();
    }
}
