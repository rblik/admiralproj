package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.repo.DepartmentRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {
    @Autowired
    private DepartmentRepository departmentRepository;

    @Override
    public Department save(@NonNull Department department) {
        return departmentRepository.save(department);
    }
}
