package io.github.panjung99.panapi.vendor.service;

import io.github.panjung99.panapi.common.dto.admin.VendorResp;
import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import io.github.panjung99.panapi.vendor.adapter.chat.ChatAdapterFactory;
import io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter;
import io.github.panjung99.panapi.vendor.entity.Vendor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VendorSyncService {

    private final VendorModelService vendorModelService;

    private final VendorService vendorService;

    private final ChatAdapterFactory chatAdapterFactory;

    @Scheduled(cron = "0 0/2 * * * ?")
    public void syncVendorModels() {
        log.info("Start synchronize all vendor's model list.");
        List<VendorResp> vendors = vendorService.getVendorsResp();
        if (vendors == null || vendors.isEmpty()) {
            log.warn("Sync vendor models fail, vendor list is empty.");
            return;
        }
        for (VendorResp vendor: vendors) {
            if (vendor.getTokens() == null || vendor.getTokens().isEmpty()) {
                log.warn("Sync vendor-{} models fail, tokens is empty.", vendor.getId());
                continue;
            }

            VendorChatAdapter adapter = chatAdapterFactory.getAdapter(vendor.getVenType());
            try {
                adapter.vendorModelsSynchronize(vendor.getId(), vendor.getTokens().get(0).getApiKey()); //TODO 改成随机取api key或更可靠的机制
            } catch (Exception e) {
                log.warn("Sync vendor-{} models fail, {}.", vendor.getId(), e.getMessage(), e);
            }
        }
        log.info("Synchronize vendors model list finish.");
    }

}
