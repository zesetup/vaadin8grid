package com.github.zesetup.vaadin8grid;

import com.github.zesetup.vaadin8grid.data.EmployeeRepository;
import com.github.zesetup.vaadin8grid.ui.EmployeeGrid;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import javax.inject.Inject;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {
  private EmployeeGrid employeeGrid;
  private EmployeeRepository repo;

  @Inject
  public VaadinUI(EmployeeRepository repo, EmployeeGrid employeeGrid) {
    this.repo = repo;
    this.employeeGrid = employeeGrid;
  }

  @Override
  protected void init(VaadinRequest request) {
    VerticalLayout mainLayout = new VerticalLayout(employeeGrid);
    setContent(mainLayout);

  }
}
