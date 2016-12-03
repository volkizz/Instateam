package com.nago.instateam.service;

import com.nago.instateam.dao.GenericDao;
import com.nago.instateam.dao.ProjectDao;
import com.nago.instateam.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl extends GenericServiceImpl<Project> implements ProjectService {
  @Autowired
  private ProjectDao projectDao;

  public ProjectServiceImpl(){}

  @Autowired
  public ProjectServiceImpl(@Qualifier("projectDaoImpl")GenericDao<Project> genericDAO) {
    super(genericDAO);
    this.projectDao = (ProjectDao) genericDAO;
  }
}
