package com.nago.instateam.service;

import java.util.List;

public interface GenericService<T> {
  List<T> findAll();
  T findById(Long id);
  void save(T type);
  void delete(T type);
}
