package com.github.zesetup.vaadin8grid.ui;

import com.github.zesetup.vaadin8grid.domain.Employee;
import com.github.zesetup.vaadin8grid.service.EmployeeService;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.VaadinSessionScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringComponent
@VaadinSessionScope
public class EmployeeGrid extends VerticalLayout {
  private static final long serialVersionUID = 1L;
  private static final Logger logger = LoggerFactory.getLogger(EmployeeGrid.class);
  private Grid<Employee> grid = new Grid<Employee>();
  private TextField filterField = new TextField();
  
  @Inject
  private EmployeeService employeeService;
  
  public EmployeeGrid() {
  }
  
  @PostConstruct
  public void  init(){
    setSizeFull();
    grid.addColumn(p -> String.valueOf(p.getName())).setCaption("Name").setSortProperty("name")
        .setWidth(200);
    grid.addColumn(p -> String.valueOf(p.getSurname())).setCaption("Surname")
        .setSortProperty("surname").setWidth(200);
    filterField.setPlaceholder("Filter by code, name..");
    // Replace listing with filtered content when user changes filter
    filterField.setValueChangeMode(ValueChangeMode.LAZY);
    Button addNewBtn = new Button("New", VaadinIcons.PLUS);
    HorizontalLayout actions = new HorizontalLayout(filterField);
    addComponents(actions, grid); 
    ConfigurableFilterDataProvider<Employee, String, Set<String>> wrapper =
        employeeService.withConfigurableFilter(
          (String queryFilter, Set<String> configuredFilters) -> {
            Set<String> combinedFilters = new HashSet<>();
            combinedFilters.addAll(configuredFilters);
            combinedFilters.add(queryFilter);
            return combinedFilters;
          }
        );
    grid.setDataProvider(wrapper); 
    
    filterField.clear();
    filterField.addValueChangeListener(e ->  {
      logger.info("filter field:" + e.getValue());
      wrapper.setFilter(Collections.singleton(e.getValue()));
      grid.getDataCommunicator().reset();
    });
  }  
}
