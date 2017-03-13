package com.github.zesetup.vaadin8grid.service;

import com.github.zesetup.vaadin8grid.domain.Employee;
import com.vaadin.data.provider.BackEndDataProvider;

import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Employee service.
 * @author Maksim Paz
 *
 */
@Service
public interface EmployeeService extends BackEndDataProvider<Employee, String> {
  void save(Employee employee);

  void update(Employee employee);

  void delete(Employee employee);

  public Employee findOne(String id);
  
  public List<Employee> findAll();

  void setFilter(String value);  
}
