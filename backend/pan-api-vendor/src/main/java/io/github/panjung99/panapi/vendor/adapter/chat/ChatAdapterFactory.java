package io.github.panjung99.panapi.vendor.adapter.chat;

import io.github.panjung99.panapi.common.enums.VenTypeEnum;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatAdapterFactory {

    private Map<VenTypeEnum, VendorChatAdapter> adapters = new HashMap<>();

    public ChatAdapterFactory(List<VendorChatAdapter> list) {
        for (VendorChatAdapter adapter: list) {
            adapters.put(adapter.getVendorType(), adapter);
        }
    }

    public VendorChatAdapter getAdapter(VenTypeEnum type) {
        return adapters.get(type);
    }
}
