package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.dto.admin.ModelCreateReq;
import io.github.panjung99.panapi.common.dto.admin.ModelUpdateReq;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.common.util.JsonUtil;
import io.github.panjung99.panapi.model.dao.ModelMapper;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.entity.PricingItem;
import io.github.panjung99.panapi.model.mapper.ModelDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelMapper modelMapper;

    private final ModelBindingService modelBindingService;

    private final PricingItemService pricingItemService;

    private final ModelDtoMapper modelDtoMapper;

    @Transactional(rollbackFor = Exception.class)
    public String createModel(ModelCreateReq req) {
        if (modelMapper.selectByName(req.getName()) != null) {
            throw new AppException(ErrorEnum.MODEL_ALREADY_EXISTS);
        }

        Model model = modelDtoMapper.toModel(req);
        model.setIsActive(1);
        modelMapper.insert(model);
        Long modelId = model.getId();
        LocalDateTime now = LocalDateTime.now();

        if (req.getVendorModelIds() != null) {
            for (Long venModelId : req.getVendorModelIds()) {
                ModelBinding binding = ModelBinding.builder()
                        .modelId(modelId)
                        .venModelId(venModelId)
                        .isActive(1)
                        .createdAt(now)
                        .updatedAt(now)
                        .build();
                modelBindingService.addBinding(binding);
            }
        }

        // 创建计费项
        if (req.getPricingItems() != null) {
            for (ModelCreateReq.PricingItemCreateReq item : req.getPricingItems()) {
                PricingItem pi = modelDtoMapper.toPricingItem(item);
                pi.setModelId(modelId);
                pi.setCurrency("CNY");//TODO 后续支持多币种
                pi.setIsActive(1);
                pricingItemService.create(pi);
            }
        }

        return "ok";
    }


    /**
     * 全量更新模型及其关联信息
     */
    @Transactional(rollbackFor = Exception.class)
    public String updateModel(Long id, ModelUpdateReq req) {
        Model model = modelMapper.selectById(id);
        if (model == null) {
            throw new AppException(ErrorEnum.MODEL_NOT_FOUND);
        }
        // 必须至少绑定一个服务商模型
        if (req.getVendorModelIds() == null || req.getVendorModelIds().isEmpty()) {
            log.error("Error updating model! At least one VendorModel binding is required. modelId:{} req:{}", id, JsonUtil.toJson(req));
            throw new AppException(ErrorEnum.INVALID_PARAMETER);
        }
        if (Boolean.FALSE.equals(req.getIsFree())
                && (req.getPricingItems() == null || req.getPricingItems().isEmpty())) {
            throw new AppException(ErrorEnum.INVALID_PARAMETER);
        }

        // 更新主表基本信息
        modelDtoMapper.updateModelFromReq(req, model);
        modelMapper.update(model);

        // 更新绑定关系
        modelBindingService.deleteBindingByModelId(id); //先删除再新增
        for (Long venModelId : req.getVendorModelIds()) {
            ModelBinding binding = new ModelBinding();
            binding.setModelId(id);
            binding.setVenModelId(venModelId);
            binding.setIsActive(1);
            modelBindingService.addBinding(binding);
        }

        // 更新计费项
        pricingItemService.deleteByModelId(id); //先删除再新增 防止传入null值跳过删除环节
        if (Boolean.FALSE.equals(req.getIsFree())) {
            for (ModelCreateReq.PricingItemCreateReq item : req.getPricingItems()) {
                PricingItem pi = modelDtoMapper.toPricingItem(item);
                pi.setModelId(id);
                pi.setCurrency("CNY"); //TODO后续支持新币种
                pi.setIsActive(1);
                pricingItemService.create(pi);
            }
        }

        return "ok";
    }

    /**
     * 修改模型的启用禁用状态
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean changeModelStatus(Long id, Boolean enabled) {
        Model model = modelMapper.selectById(id);
        if (model == null) {
            throw new AppException(ErrorEnum.MODEL_NOT_FOUND);
        }
        Integer status = enabled? 1: 0;
        model.setIsActive(status);

        int rows = modelMapper.update(model);
        return rows > 0;
    }


    public Model getActiveModelByName(String modelName) {
        return modelMapper.selectActiveByName(modelName);
    }

    /**
     * 获取用于Api展示的模型实体类列表(下架模型不包括)
     * @return
     */
    public List<ModelResp> getApiModelList() {
        List<Model> all = modelMapper.selectApiActive();
        return all.stream().map(model -> {
            ModelResp resp = modelDtoMapper.toModelResp(model);

            List<PricingItem> pricingItems = pricingItemService.getByModelId(model.getId());
            resp.setPricingItems(modelDtoMapper.toPricingItemRespList(pricingItems));
            return resp;
        }).toList();
    }

    /**
     * 获取用于Api展示的模型实体类列表（包括已下架模型）,以及模型绑定关系。
     * @return
     */
    public List<ModelResp> getAdminModelList() {
        List<Model> allModels = modelMapper.selectApiAll();
        return allModels.stream().map(model -> {
            ModelResp resp = modelDtoMapper.toModelResp(model);
            List<ModelBinding> bindings = modelBindingService.getAllByModelId(model.getId());
            resp.setBindings(modelDtoMapper.toBindingRespList(bindings));

            List<PricingItem> pricingItems = pricingItemService.getByModelId(model.getId());
            resp.setPricingItems(modelDtoMapper.toPricingItemRespList(pricingItems));

            return resp;
        }).toList();
    }
}
