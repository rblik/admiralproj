package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;
import isr.naya.admiralproj.repository.DepartmentRepository;
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
public class DepartmentServiceImpl implements DepartmentService {

    private DepartmentRepository departmentRepository;

    @CacheEvict(value = {"departments", "employees"}, allEntries = true)
    @Override
    @Transactional
    public Department save(@NonNull Department department) {
        return departmentRepository.save(department);
    }

    @Cacheable(value = "departments", key = "getMethodName()")
    @Override
    public List<Department> getAll() {
        return departmentRepository.findAllByOrderByName();
    }

    @Cacheable(value = "departments", key = "getMethodName() + #id")
    @Override
    public Department get(@NonNull Integer id) {
        return checkNotFound(departmentRepository.findOne(id), id, Department.class);
    }
}
