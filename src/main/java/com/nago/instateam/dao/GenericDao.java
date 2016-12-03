package com.nago.instateam.dao;


import java.util.List;

public interface GenericDao<T> {
  List<T> findAll();
  T findById(Long id);
  void save(T type);
  void delete(T type);
}
