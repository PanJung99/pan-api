package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.dao.ModelBindingMapper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ModelBindingService {

    @Autowired
    private ModelBindingMapper mapper;

    public int addBinding(ModelBinding binding) {
        return mapper.insert(binding);
    }

    public int enableBinding(Long id, boolean enabled) {
        return mapper.updateEnabled(id, enabled);
    }

    public int deleteBinding(Long id) {
        return mapper.deleteById(id);
    }

    public ModelBinding getById(Long id) {
        return mapper.selectById(id);
    }

    public List<ModelBinding> getByModelId(Long modelId) {
        return mapper.selectByModelId(modelId);
    }

    public List<ModelBinding> getByVenModelId(Long venModelId) {
        return mapper.selectByVenModelId(venModelId);
    }

    public ModelBinding getByModelAndVendor(Long modelId, Long venModelId) {
        return mapper.selectByModelAndVendor(modelId, venModelId);
    }
}
