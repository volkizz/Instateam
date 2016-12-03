package com.nago.instateam.service;

import com.nago.instateam.dao.CollaboratorDao;
import com.nago.instateam.dao.GenericDao;
import com.nago.instateam.model.Collaborator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class CollaboratorServiceImpl extends GenericServiceImpl<Collaborator> implements CollaboratorService {
  @Autowired
  private CollaboratorDao collaboratorDao;

  public CollaboratorServiceImpl(){}

  @Autowired
  public CollaboratorServiceImpl(@Qualifier("collaboratorDaoImpl")GenericDao<Collaborator> genericDAO) {
    super(genericDAO);
    this.collaboratorDao = (CollaboratorDao) genericDAO;
  }
}
