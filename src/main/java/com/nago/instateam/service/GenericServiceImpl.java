package com.nago.instateam.service;

import com.nago.instateam.dao.GenericDao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public abstract class GenericServiceImpl<T> implements GenericService<T> {
  private GenericDao<T> genericDAO;

  public GenericServiceImpl(GenericDao<T> genericDAO) {
    this.genericDAO = genericDAO;
  }

  public GenericServiceImpl(){}

  @Override
  public List<T> findAll() {
    return genericDAO.findAll();
  }

  @Override
  public T findById(Long id) {
    return genericDAO.findById(id);
  }

  @Override
  public void save(T type) {
    genericDAO.save(type);
  }

  @Override
  public void delete(T type) {
    genericDAO.delete(type);
  }
}