package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.repository.ClientRepository;
import isr.naya.admiralproj.repository.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.util.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private ClientRepository clientRepository;

    @CacheEvict(value = {"projects", "clients"}, allEntries = true)
    @Override
    @Transactional
    public Project save(@NonNull Integer clientId, @NonNull Project project) {
        project.setClient(checkNotFound(clientRepository.findOne(clientId), clientId, Client.class));
        return projectRepository.save(project);
    }

    @Cacheable(value = "projects", key = "getMethodName()")
    @Override
    public List<Project> getAllWithClients() {
        return projectRepository.getAllWithClients();
    }

    @Cacheable(value = "projects", key = "getMethodName() + #id")
    @Override
    public Project get(@NonNull Integer id) {
        return checkNotFound(projectRepository.findOne(id), id, Project.class);
    }

    @Cacheable(value = "projects", key = "getMethodName() + #id")
    @Override
    public Project getWithClient(@NonNull Integer id) {
        return checkNotFound(projectRepository.getOneWithClient(id), id, Project.class);
    }
}
