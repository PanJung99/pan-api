package io.github.panjung99.panapi.vendor.service;

import io.github.panjung99.panapi.common.dto.admin.VendorCreateReq;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.dao.VendorMapper;
import io.github.panjung99.panapi.common.dto.admin.VendorResp;
import io.github.panjung99.panapi.vendor.entity.Vendor;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorService {

    private final VendorMapper vendorMapper;

    /**
     * Retrieves the type {@link VenTypeEnum} of the vendor by id.
     * @param vendorId vendor id
     * @return venTypeEnum, if not found vendor or param is null.
     */
    @Cacheable(value = "vendorType", key = "#vendorId")
    public VenTypeEnum getVendorType(Long vendorId) {
        Vendor vendor = vendorMapper.selectById(vendorId);
        if (vendor == null) {
            return null;
        }
        return vendor.getVenType();
    }

    /**
     * Retrieves the vendor info by id.
     */
    @Cacheable(value = "vendor", key = "#id")
    public Vendor getVendorById(Long id) {
        return vendorMapper.selectById(id);
    }

    /**
     * Retrieves all vendors with total tokens.
     */
    public List<VendorResp> getVendorsResp() {
        return vendorMapper.selectVendorsWithTokens();
    }

    /**
     * Create new Vendor.
     */
    @CacheEvict(value = {"vendor", "vendorType"}, allEntries = true)
    public void createVendor(VendorCreateReq req) {
        Vendor vendor = Vendor.builder()
                .name(req.getName())
                .apiBaseUrl(req.getApiBaseUrl())
                .venType(req.getVenType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        vendorMapper.insert(vendor);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteVendor(Long vendorId) {
        //TODO 待开发 删除vendor和下面的全部token 要注意批量调用deleteToken会反复调用webClientProvider.refreshVendorWebClients();

        return false;
    }


}
