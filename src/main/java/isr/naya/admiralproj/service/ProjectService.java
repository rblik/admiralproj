package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Project;

import java.util.List;

public interface ProjectService {

    Project save(Integer clientId, Project project);

    List<Project> getAll();
}
