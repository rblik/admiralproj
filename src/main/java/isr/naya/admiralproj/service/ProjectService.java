package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Project;

import java.util.List;

public interface ProjectService {

    Project save(Integer clientId, Project project);

    List<Project> getAllWithClients();

    List<Project> getAllWithClientsByEmployee(Integer employeeId);

    Project get(Integer id);

    Project getWithClient(Integer id);
}
