package com.github.zesetup.vaadin8grid.service;

import com.github.zesetup.vaadin8grid.data.EmployeeRepository;
import com.github.zesetup.vaadin8grid.domain.Employee;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.Query;
import com.vaadin.shared.Registration;
import com.vaadin.shared.data.sort.SortDirection;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


/**
 * employee service.
 * 
 * @author Maksim Paz
 *
 */
@Component
@Transactional
public class EmployeeServiceImpl  implements EmployeeService {
  private static final long serialVersionUID = 1L;

  private static class PageQuery {
    Pageable pageable;
    int pageOffset;
  }

  private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceImpl.class);

  @Inject
  EmployeeRepository employeeRepository;

  private String textFilter;


  public void save(Employee employee) {
    employeeRepository.save(employee);
  }

  @Override
  public void update(Employee employee) {
    employeeRepository.save(employee);
  }

  @Override
  public void delete(Employee employee) {
    employeeRepository.delete(employee);
  }

  @Override
  public Employee findOne(String id) {
    return employeeRepository.findOne(id);
  }

  @Override
  public List<Employee> findAll() {
    return employeeRepository.findAll();
  }
  
 
  @Override
  public void refreshAll() {
    logger.info("refreshAll() fired");
  }
  
  @Override
  public Stream<Employee> fetch(Query query) {
    logger.info("Fetch fired, getFilter(query):" + getFilter(query));
    PageQuery pageQuery = getPaging(query);
    //Stream<employee> stream = getItems(pageQuery.pageable, getFilter(query))
    Stream<Employee> stream = getItems(pageQuery.pageable, textFilter)
        .skip(pageQuery.pageOffset).limit(query.getLimit());
    logger.info("Fetched count:" + getItems(pageQuery.pageable, "").count());
    return stream;
  }
  
  private Stream<Employee> getItems(Pageable page, String filterText) {
    if (filterText == null || filterText.isEmpty()) {
      Page<Employee> result2 = employeeRepository.findAll(page);
      logger.info("Filter NUll loaded items size:" + result2.getNumberOfElements());
      return StreamSupport.stream(result2.spliterator(), false);
    }
    Page<Employee> result = employeeRepository.findByNameIgnoringCaseContaining(filterText, page);
    return StreamSupport.stream(result.spliterator(), false);
  }

  // BackEndDataProvider methods:
  protected String getFilter(Query query) {
    String filter = null ;
    if (!query.getFilter().isPresent()){
      return null;
    }
    return filter;
  }

  /**
   * Return a PageQuery object containing page request and offset in page.
   *
   * @param q the original query
   * @return paged query
   */
  private PageQuery getPaging(Query  q) {
    final PageQuery p = new PageQuery();
    int start = q.getOffset();
    int end = q.getOffset() + q.getLimit();

    if (start < end - start) {
      p.pageable = getPageRequest(q, 0, end);
      p.pageOffset = q.getOffset();
    } else {
      // Calculate the page that fits the full requested index range
      int size = end - start;
      while (start / size != (end - 1) / size) {
        ++size;
      }
      p.pageable = getPageRequest(q, start / size, size);
      // Set the offset on page to filter out unneeded results
      p.pageOffset = start % size;
    }

    return p;
  }
  
  private PageRequest getPageRequest(
      Query<Employee, Set<String>> q,
      int pageIndex,
      int pageLength) {
    if (!q.getSortOrders().isEmpty()) {
      return new PageRequest(pageIndex, pageLength, getSorting(q));
    } else {
      return new PageRequest(pageIndex, pageLength);
    }
  }

  private Sort getSorting(Query<Employee, Set<String>> q) {
    return new Sort(q.getSortOrders().stream()
        .map(so -> new Order(
            so.getDirection() == SortDirection.ASCENDING ? Direction.ASC : Direction.DESC,
            so.getSorted()))
        .collect(Collectors.toList()));
  }
 
  @Override
  public void setSortOrders(List sortOrders) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public int size(Query query) {
    if (textFilter == null) {
      return (int) employeeRepository.count();
    } else {
      return (int) employeeRepository.countByNameIgnoringCaseContaining(textFilter);
    }
       
  }

  @Override
  public void setFilter(String value) {
    this.textFilter = value;
  }

  @Override
  public void refreshItem(Employee item) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public Registration addDataProviderListener(DataProviderListener<Employee> listener) {
    // TODO Auto-generated method stub
    return null;
  }

}