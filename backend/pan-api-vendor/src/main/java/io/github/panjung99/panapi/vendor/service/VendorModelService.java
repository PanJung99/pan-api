package io.github.panjung99.panapi.vendor.service;

import io.github.panjung99.panapi.common.dto.admin.VendorModelResp;
import io.github.panjung99.panapi.vendor.dao.VendorModelMapper;
import io.github.panjung99.panapi.vendor.entity.VendorModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorModelService {

    private final VendorModelMapper vendorModelMapper;

    /**
     * Retrieves vendor model by id.
     */
    public VendorModel getModelById(Long id) {
        return vendorModelMapper.selectById(id);
    }

    /**
     * Retrieves all vendor models.
     * @return
     */
    public List<VendorModelResp> getModels() {
        List<VendorModel> vendorModels = vendorModelMapper.selectAll();
        if (vendorModels != null) {
            return vendorModels.stream().map(t -> {
                VendorModelResp resp = new VendorModelResp();
                resp.setId(t.getId());
                resp.setName(t.getName());
                resp.setVendorId(t.getVendorId());
                return resp;
            }).toList();
        }
        return null;
    }

    /**
     * Synchronize all models for the vendor, create new one if not exists, update exists one.
     * @param vendorId vendor id
     * @param vendorModels The latest model list from Vendor ModelList endpoint.
     */
    public void updateVendorModels(Long vendorId, List<VendorModel> vendorModels) {
        List<VendorModel> oldVendorModels = vendorModelMapper.selectAllByVendorId(vendorId);

        Map<String, VendorModel> oldModels = oldVendorModels.stream()
                .collect(Collectors.toMap(VendorModel::getName, m -> m));

        Map<String, VendorModel> newModelIds = vendorModels.stream()
                .collect(Collectors.toMap(VendorModel::getName, m -> m));

        for (VendorModel newItem: vendorModels) {
            if (oldModels.containsKey(newItem.getName())) {
                // If model exists, update and clear the logical delete mark.
                newItem.setIsDeleted(0);
                vendorModelMapper.update(newItem);
            } else {
                // Create new Vendor Model if not exists.
                vendorModelMapper.insert(newItem);
            }
        }

        for (VendorModel oldItem: oldVendorModels) {
            if (!newModelIds.containsKey(oldItem.getName())) {
                // Delete models if not in latest Vendor ModelList.
                LocalDateTime updateTime = LocalDateTime.now();
                vendorModelMapper.logicalDelete(oldItem.getId(), updateTime);
            }
        }

    }
}
