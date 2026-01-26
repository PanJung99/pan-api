package io.github.panjung99.panapi.vendor.service;

import io.github.panjung99.panapi.vendor.dao.VendorMapper;
import io.github.panjung99.panapi.common.dto.admin.VendorTokenReq;
import io.github.panjung99.panapi.common.dto.admin.VendorResp;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


/**
 * Manages WebClient instances by maintaining the latest Vendor-VendorToken-WebClient mapping from the database.
 * And provides corresponding webClient for {@link io.github.panjung99.panapi.vendor.adapter.chat.VendorChatAdapter} implementations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientProvider {

    private final VendorMapper vendorMapper;

    /**
     * vendor-vendorToken-webclient mapping.
     */
    private static final AtomicReference<ConcurrentHashMap<Long, Map<String, WebClient>>> webClientMapRef
            = new AtomicReference<>();

    @PostConstruct
    public void init() {
        refreshVendorWebClients();
    }

    /**
     * Refresh every 5 minutes to ensure newly insert token record take effect.
     * Should also invoke this method manually after CRUD operations with Vendors and VendorTokens.
     * TODO: 扩展为多实例时，应注意该刷新机制会失效。
     */
    @Scheduled(cron = "0 0/5 * * * ?")
    public void refreshVendorWebClients() {
        ConcurrentHashMap<Long, Map<String, WebClient>> newMap = loadWebClients();
        webClientMapRef.set(newMap);
    }


    /**
     * Provides corresponding WebClient instances using vendorId and Token.
     * @param vendorId vendor's id
     * @param token vendor's api key
     * @return Corresponding vendor webClient, null if it does not exist.
     */
    public WebClient getWebClient(Long vendorId, String token) {
        Map<Long, Map<String, WebClient>> clientsMap = webClientMapRef.get();
        Map<String, WebClient> tokenMap = clientsMap.get(vendorId);
        if (tokenMap == null || tokenMap.isEmpty() || !tokenMap.containsKey(token)) {
            log.warn("WebClient not found for Vendor-{} Token-{}.", vendorId, token);
            return null;
        }
        return tokenMap.get(token);
    }

    /**
     * Loads vendors and their tokens, then initializes WebClients.
     * @return vendor-vendorToken-webclient mapping.
     */
    private ConcurrentHashMap<Long, Map<String, WebClient>> loadWebClients() {
        ConcurrentHashMap<Long, Map<String, WebClient>> vendorMap = new ConcurrentHashMap<>();
        List<VendorResp> vendorList = vendorMapper.selectVendorsWithTokens();

        for (VendorResp vendorResp: vendorList) {
            if (vendorResp.getTokens() == null || vendorResp.getTokens().isEmpty()) {
                log.warn("Vendor {} has no tokens", vendorResp.getId());
                continue;
            }

            Map<String, WebClient> tokenMap = new ConcurrentHashMap<>();
            for (VendorTokenReq token : vendorResp.getTokens()) {
                if (token == null || token.getApiKey() == null || token.getApiKey().isEmpty()) {
                    log.warn("Found null token in vendor {}", vendorResp.getId());
                    continue;
                }
                // Every token will create a WebClient.
                WebClient webClient = createWebClient(vendorResp.getApiBaseUrl(), token.getApiKey());
                tokenMap.put(token.getApiKey(), webClient);
            }
            vendorMap.put(vendorResp.getId(), tokenMap);
        }

        return vendorMap;
    }


    /**
     * Creates a webClient and configures base Url and authorization header(api key).
     * @param baseUrl vendor base url.
     * @param apiKey vendor api key.
     * @return webClient
     */
    private WebClient createWebClient(String baseUrl, String apiKey) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeaders(headers -> {
                    headers.set("Authorization", "Bearer " + apiKey);
                    headers.set("Content-Type", "application/json");
                })
                .build();
    }



}
