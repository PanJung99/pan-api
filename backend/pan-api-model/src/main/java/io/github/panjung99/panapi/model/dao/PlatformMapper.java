package io.github.panjung99.panapi.model.dao;

import io.github.panjung99.panapi.model.entity.ModelType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PlatformMapper {

    List<ModelType> findAll();

    ModelType findById(@Param("id") Long id);

    int insert(ModelType modelType);

    int update(ModelType modelType);

    int softDelete(@Param("id") Long id);
}