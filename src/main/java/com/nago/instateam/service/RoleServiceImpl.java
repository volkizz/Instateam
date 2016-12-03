package com.nago.instateam.service;

import com.nago.instateam.dao.GenericDao;
import com.nago.instateam.dao.RoleDao;
import com.nago.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends GenericServiceImpl<Role> implements RoleService{

  @Autowired
  private RoleDao roleDao;

  public RoleServiceImpl(){}

  @Autowired
  public RoleServiceImpl(@Qualifier("roleDaoImpl")GenericDao<Role> genericDAO) {
    super(genericDAO);
    this.roleDao = (RoleDao) genericDAO;
  }
}
