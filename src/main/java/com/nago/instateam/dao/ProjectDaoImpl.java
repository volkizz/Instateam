package com.nago.instateam.dao;

import com.nago.instateam.model.Project;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDaoImpl extends GenericDaoImpl<Project> implements ProjectDao {
  @Autowired
  private SessionFactory sessionFactory;

  @Override
  @SuppressWarnings("unchecked")
  public List<Project> findAll() {
    Session session = sessionFactory.openSession();
    List<Project> projects = session.createCriteria(Project.class).list();
    for (Project project : projects) {
      Hibernate.initialize(project.getCollaborators());
      Hibernate.initialize(project.getRolesNeeded());
    }
    session.close();
    return projects;
  }

  @Override
  public Project findById(Long id) {
    Session session = sessionFactory.openSession();
    Project project = session.get(Project.class, id);
    Hibernate.initialize(project.getRolesNeeded());
    Hibernate.initialize(project.getCollaborators());
    project.getRolesNeeded().forEach(
        r -> Hibernate.initialize(r.getCollaborators())
    );
    session.close();
    return project;
  }
}
