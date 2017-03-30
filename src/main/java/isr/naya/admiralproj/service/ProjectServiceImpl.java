package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.repo.ClientRepository;
import isr.naya.admiralproj.repo.ProjectRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;
    private ClientRepository clientRepository;

    @Override
    @Transactional
    public Project save(@NonNull Integer clientId, @NonNull Project project) {
        project.setClient(checkNotFound(clientRepository.findOne(clientId), clientId, Client.class));
        return projectRepository.save(project);
    }

    @Override
    public List<Project> getAllWithClients() {
        return projectRepository.getAllWithClients();
    }

    @Override
    public Project get(Integer id) {
        return checkNotFound(projectRepository.findOne(id), id, Project.class);
    }

    @Override
    public Project getWithClient(Integer id) {
        return checkNotFound(projectRepository.getOneWithClient(id), id, Project.class);
    }
}
