package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.dao.ModelBindingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelBindingService {

    private final ModelBindingMapper mapper;

    public int addBinding(ModelBinding binding) {
        return mapper.insert(binding);
    }

    /**
     * Changes the status of model binding.
     * @param id Entity ID
     * @param enabled enable or disable
     * @return success or not
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeBindingStatus(Long id, Boolean enabled) {
        ModelBinding binding = mapper.selectById(id);
        if (binding == null) {
            throw new AppException(ErrorEnum.BINDING_NOT_FOUND);
        }
        Integer status = enabled? 1: 0;
        binding.setIsActive(status);

        int rows = mapper.updateEnabled(id, status, LocalDateTime.now());
        return rows > 0;
    }

    public int deleteBindingByModelId(Long modelId) {
        return mapper.deleteByModelId(modelId);
    }

    public ModelBinding getById(Long id) {
        return mapper.selectById(id);
    }

    public List<ModelBinding> getByModelId(Long modelId) {
        return mapper.selectByModelId(modelId);
    }

    public List<ModelBinding> getAllByModelId(Long modelId) {
        return mapper.selectAllByModelId(modelId);
    }
}
