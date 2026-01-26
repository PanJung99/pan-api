package io.github.panjung99.panapi.model.service;

import io.github.panjung99.panapi.common.dto.admin.ModelCreateReq;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.model.dao.ModelMapper;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.entity.PricingItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModelService {

    private final ModelMapper modelMapper;

    private final ModelBindingService modelBindingService;

    private final PricingItemService pricingItemService;

    @Transactional(rollbackFor = Exception.class)
    public String createModel(ModelCreateReq req) {

        Model model = new Model();
        model.setName(req.getName());
        model.setDisplayName(req.getDisplayName());
        model.setIsFree(req.getIsFree());
        model.setCategory(req.getCategory());
        model.setPlatformType(req.getPlatformType());
        model.setDescription(req.getDescription());
        model.setIsActive(1);
        modelMapper.insert(model);

        Long modelId = model.getId();

        if (req.getVendorModelIds() != null) {
            for (Long venModelId : req.getVendorModelIds()) {
                ModelBinding binding = new ModelBinding();
                binding.setModelId(modelId);
                binding.setVenModelId(venModelId);
                binding.setEnabled(1);
                modelBindingService.addBinding(binding);
            }
        }

        // 3. 创建计费项
        if (req.getPricingItems() != null) {
            for (ModelCreateReq.PricingItemCreateReq item : req.getPricingItems()) {
                PricingItem pi = new PricingItem();
                pi.setModelId(modelId);
                pi.setUnit(item.getUnit());
                pi.setPriceInput(item.getPriceInput());
                pi.setPriceOutput(item.getPriceOutput());
                pi.setCurrency("CNY");//TODO 后续支持多币种
                pi.setIsActive(1);
                pi.setIsDeleted(0);
                pricingItemService.create(pi);
            }
        }

        return "ok";
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
        List<ModelResp> result = all.stream()
                .map(this::toResp)
                .toList();
        return result;
    }

    /**
     * 获取用于Api展示的模型实体类列表（包括已下架模型）,以及模型绑定关系。
     * @return
     */
    public List<ModelResp> getToBModelList() {
        List<Model> allModels = modelMapper.selectApiAll();
        return allModels.stream().map(model -> {
            ModelResp resp = toResp(model);
            // 查询该模型的绑定关系
            List<ModelBinding> bindings = modelBindingService.getByModelId(model.getId());
            resp.setBindings(bindings.stream().map(this::toBindingDetail).toList());

            // 查询模型定价
            List<PricingItem> pricingItems = pricingItemService.getByModelId(model.getId());
            resp.setPricingItems(pricingItems.stream().map(this::toPricingItemResp).toList());

            return resp;
        }).toList();
    }


    private ModelResp toResp(Model model) {
        ModelResp dto = new ModelResp();
        dto.setName(model.getName());
        dto.setDisplayName(model.getDisplayName());
        dto.setIsFree(model.getIsFree());
        dto.setCategory(model.getCategory());
        dto.setDescription(model.getDescription());
        return dto;
    }

    private ModelResp.BindingDetail toBindingDetail(ModelBinding b) {
        ModelResp.BindingDetail detail = new ModelResp.BindingDetail();
        detail.setId(b.getId());
        detail.setVenModelId(b.getVenModelId());
        detail.setModelId(b.getModelId());
        detail.setEnabled(b.getEnabled());
        return detail;
    }

    private ModelResp.PricingItemResp toPricingItemResp(PricingItem item) {
        ModelResp.PricingItemResp vo = new ModelResp.PricingItemResp();
        vo.setId(item.getId());
        vo.setUnit(item.getUnit());
        vo.setPriceInput(item.getPriceInput());
        vo.setPriceOutput(item.getPriceOutput());
        vo.setCurrency(item.getCurrency());
        vo.setIsActive(item.getIsActive());
        return vo;
    }
}
