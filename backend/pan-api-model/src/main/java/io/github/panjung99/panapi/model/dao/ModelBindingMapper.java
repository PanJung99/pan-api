package io.github.panjung99.panapi.model.dao;

import io.github.panjung99.panapi.model.entity.ModelBinding;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ModelBindingMapper {

    int insert(ModelBinding modelBinding);

    int updateEnabled(@Param("id") Long id,
                      @Param("isActive") Integer isActive,
                      @Param("updatedAt") LocalDateTime updatedAt);

    int deleteByModelId(Long modelId);

    ModelBinding selectById(Long id);

    List<ModelBinding> selectByModelId(Long modelId);

    List<ModelBinding> selectAllByModelId(Long modelId);
}
