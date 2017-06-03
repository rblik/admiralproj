package isr.naya.admiralproj.web.controllers.admin;

import isr.naya.admiralproj.AuthorizedUser;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.service.ProjectService;
import isr.naya.admiralproj.web.controllers.CorsRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@CorsRestController
@RequestMapping("/backend/admin/projects")
@AllArgsConstructor
public class AdminProjectsController {

    private ProjectService projectService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Project> saveProject(@AuthenticationPrincipal AuthorizedUser admin,
                                               @Valid @RequestBody Project project,
                                               @RequestParam("clientId") Integer clientId) {
        Project saved = projectService.save(clientId, project);
        log.info("Admin {} saved a new project {} with id = {} for client (id = {})", admin.getFullName(), saved.getName(), saved.getId(), clientId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<Project> getAllProjects(@AuthenticationPrincipal AuthorizedUser admin) {
        List<Project> projects = projectService.getAllWithClients();
        log.info("Admin {} is retrieving all projects", admin.getFullName());
        return projects;
    }

    @GetMapping(params = {"employeeId"})
    public List<Project> getAllProjects(@AuthenticationPrincipal AuthorizedUser admin,
                                        @RequestParam("employeeId") Integer employeeId) {
        List<Project> projects = projectService.getAllWithClientsByEmployee(employeeId);
        log.info("Admin {} is retrieving all projects for employee (id = {})", admin.getFullName(), employeeId);
        return projects;
    }

    @GetMapping("/{projectId}")
    public Project getProject(@AuthenticationPrincipal AuthorizedUser admin,
                              @PathVariable("projectId") Integer projectId) {
        Project project = projectService.getWithClient(projectId);
        log.info("Admin {} is retrieving project with id = {}", admin.getFullName(), projectId);
        return project;
    }
}
