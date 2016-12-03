package com.nago.instateam.converter;

import com.nago.instateam.dao.RoleDao;
import com.nago.instateam.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class StringRoleConverter implements Converter<String, Role> {

  @Autowired
  private RoleDao roleDao;

  @Override
  public Role convert(String source) {
    return roleDao.findById(
        new Long(source)
    );
  }

  @Bean
  public ConversionService getConversionService() {
    ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
    Set<Converter> converters = new HashSet<>();
    converters.add(new StringRoleConverter());
    bean.setConverters(converters);
    return bean.getObject();
  }
}