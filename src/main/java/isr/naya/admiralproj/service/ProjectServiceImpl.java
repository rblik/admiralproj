package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Client;
import isr.naya.admiralproj.model.Project;
import isr.naya.admiralproj.repo.ClientRepository;
import isr.naya.admiralproj.repo.ProjectRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static isr.naya.admiralproj.exception.ValidationUtil.checkNotFound;

@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public Project save(@NonNull Integer clientId, @NonNull Project project) {
        project.setClient(checkNotFound(clientRepository.findOne(clientId), clientId, Client.class));
        return projectRepository.save(project);
    }
}
