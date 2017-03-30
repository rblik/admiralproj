package isr.naya.admiralproj.service;

import isr.naya.admiralproj.model.Department;

import java.util.List;

public interface DepartmentService {

    Department save(Department department);

    List<Department> getAll();

    Department get(Integer id);
}
