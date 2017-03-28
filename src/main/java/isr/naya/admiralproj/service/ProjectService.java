package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Project;

public interface ProjectService {

    Project save(Integer clientId, Project project);
}
