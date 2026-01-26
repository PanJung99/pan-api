package io.github.panjung99.panapi.model.dao;

import io.github.panjung99.panapi.model.entity.ModelBinding;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ModelBindingMapper {

    int insert(ModelBinding modelBinding);

    int updateEnabled(Long id, Boolean enabled);

    int deleteById(Long id);

    ModelBinding selectById(Long id);

    List<ModelBinding> selectByModelId(Long modelId);

    List<ModelBinding> selectByVenModelId(Long venModelId);

    ModelBinding selectByModelAndVendor(Long modelId, Long venModelId);
}
