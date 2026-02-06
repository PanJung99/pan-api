package io.github.panjung99.panapi.model.mapper;

import io.github.panjung99.panapi.common.dto.admin.ModelCreateReq;
import io.github.panjung99.panapi.common.dto.admin.ModelUpdateReq;
import io.github.panjung99.panapi.common.dto.be.ModelResp;
import io.github.panjung99.panapi.model.entity.Model;
import io.github.panjung99.panapi.model.entity.ModelBinding;
import io.github.panjung99.panapi.model.entity.PricingItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModelDtoMapper {

    ModelResp toModelResp(Model model);

    ModelResp.BindingDetail toBindingResp(ModelBinding binding);

    ModelResp.PricingItemResp toPricingItemResp(PricingItem item);

    List<ModelResp.BindingDetail> toBindingRespList(List<ModelBinding> bindings);

    List<ModelResp.PricingItemResp> toPricingItemRespList(List<PricingItem> items);

    Model toModel(ModelCreateReq req);

    void updateModelFromReq(ModelUpdateReq req, @MappingTarget Model model);

    PricingItem toPricingItem(ModelCreateReq.PricingItemCreateReq req);
}