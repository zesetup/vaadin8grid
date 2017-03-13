package com.github.zesetup.vaadin8grid.data;

import com.github.zesetup.vaadin8grid.domain.Employee;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Employee repo.
 * @author Maksim Paz
 *
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {
  
  List<Employee> findByNameIgnoringCaseContaining(String textPattern);

  Page<Employee> findByNameIgnoringCaseContaining(String textPattern, Pageable pageable);
  
  long countByNameIgnoringCaseContaining(String textPattern);
}

