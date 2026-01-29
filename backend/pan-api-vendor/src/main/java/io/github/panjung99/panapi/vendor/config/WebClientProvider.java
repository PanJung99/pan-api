package io.github.panjung99.panapi.vendor.config;

import io.github.panjung99.panapi.vendor.entity.Vendor;
import io.github.panjung99.panapi.vendor.service.VendorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Manages WebClient instances by maintaining the latest Vendor-VendorToken-WebClient mapping from the database.
 * And provides corresponding webClient for {@link io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter} implementations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientProvider {

    private final VendorService vendorService;

    private final WebClient baseWebClient;


    /**
     * Provides corresponding WebClient instances using vendorId and Token.
     * @param vendorId vendor's id
     * @param token vendor's api key
     * @return Corresponding vendor webClient, null if it does not exist.
     */
    public WebClient getWebClient(Long vendorId, String token) {
        Vendor vendor = vendorService.getVendorById(vendorId);
        if (vendor == null) {
            log.warn("WebClient not found for Vendor-{} Token-{}.", vendorId, token);
            return null;
        }
        return baseWebClient.mutate()
                .baseUrl(vendor.getApiBaseUrl())
                .defaultHeaders(headers -> headers.set("Authorization", "Bearer " + token))
                .build();
    }

}
