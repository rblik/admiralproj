package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.repo.DepartmentRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @Override
    public Department save(@NonNull Department department) {
        return departmentRepository.save(department);
    }
}
