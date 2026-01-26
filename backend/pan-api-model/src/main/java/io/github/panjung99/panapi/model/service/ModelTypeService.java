package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.model.dao.PlatformMapper;
import io.github.panjung99.panapi.model.entity.ModelType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelTypeService {

    @Autowired
    private PlatformMapper mapper;

    public List<ModelType> listAll() {
        return mapper.findAll();
    }

    public ModelType getById(Long id) {
        return mapper.findById(id);
    }

}
