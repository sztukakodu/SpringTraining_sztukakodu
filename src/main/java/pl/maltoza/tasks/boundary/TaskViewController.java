package pl.maltoza.tasks.boundary;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.maltoza.tasks.control.TasksService;
import pl.maltoza.tasks.entity.Task;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static java.util.stream.Collectors.toList;

@Slf4j
@Controller
public class TaskViewController {

    private final TasksService tasksService;
    private final StorageService storageService;

    @Autowired
    TaskViewController(TasksService tasksService, StorageService storageService) {
        this.tasksService = tasksService;
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("tasks", tasksService.fetchAll().stream().map(TaskViewResponse::from).collect(toList()));
        model.addAttribute("newTask", new CreateTaskRequest());
        return "home";
    }

    @PostMapping("/tasks")
    public String addTask(@ModelAttribute("newTask") CreateTaskRequest request,
                          @RequestParam("attachment") MultipartFile attachment) throws IOException {
        log.info("adding task to view time...");
        Task task = tasksService.addTask(request.title, request.description, Collections.emptySet());
        tasksService.addTaskAttachment(task.getId(),attachment, request.attachmentComment);
        return "redirect:/";
    }

    @PostMapping("/tasks/delete/{id}")
    public String removeTask(@PathVariable Long id) {
        tasksService.removeTask(id);
        return "redirect:/";
    }


}
