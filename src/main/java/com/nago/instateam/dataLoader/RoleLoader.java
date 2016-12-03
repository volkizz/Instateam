package com.nago.instateam.dataLoader;

import com.nago.instateam.dao.RoleDao;
import com.nago.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
This class is used to setup role "Unassigned".
Its needed for deleting roles without leaving collaborators with empty role field.
So when role is deleted, collaborator who had it will get "Unassigned" role,
which can be easily change when editing collaborators.
 */
@Component
public class RoleLoader implements ApplicationRunner {
  @Autowired
  private RoleDao roleDao;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    Role role = new Role();
    role.setName("Unassigned");
    boolean exist = false;

    for(Role r : roleDao.findAll()){
      if(Objects.equals(r.getName(), role.getName())){
        exist= true;
      }
    }
    if(!exist){
      roleDao.save(role);
    }
  }
}
