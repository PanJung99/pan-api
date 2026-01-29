package io.github.panjung99.panapi.vendor.service;

import io.github.panjung99.panapi.common.dto.admin.VendorTokenCreateReq;
import io.github.panjung99.panapi.common.dto.admin.VendorTokenUpdateReq;
import io.github.panjung99.panapi.common.exceptions.AppException;
import io.github.panjung99.panapi.common.exceptions.ErrorEnum;
import io.github.panjung99.panapi.vendor.dao.VendorTokenMapper;
import io.github.panjung99.panapi.vendor.entity.VendorToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VendorTokenService {

    private final VendorTokenMapper vendorTokenMapper;

    /**
     * TODO token路由功能应完善并迁移到router模块中
     * @param vendorId
     * @return
     */
    public VendorToken getRandomVendorToken(Long vendorId) {
        List<VendorToken> tokenList = vendorTokenMapper.selectActiveByVendorId(vendorId);
        if (tokenList.isEmpty()) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(tokenList.size());
        return tokenList.get(index);
    }


    /**
     * Creates a new Token and refreshes the vendor WebClient cache
     */
    public void createToken(Long vendorId, VendorTokenCreateReq req) {
        List<VendorToken> list = vendorTokenMapper.selectByVendorAndKey(vendorId, req.getApiKey());
        if (list != null && !list.isEmpty()) {
            throw new AppException(ErrorEnum.TOKEN_ALREADY_EXISTS);
        }
        VendorToken token = new VendorToken();
        token.setVendorId(vendorId);
        token.setApiKey(req.getApiKey());
        token.setTokenName(req.getTokenName());
        token.setIsActive(1L);
        token.setExpiresAt(req.getExpiresAt());
        vendorTokenMapper.insert(token);
    }


    /**
     * Updates the Token by vendor id and refreshes the vendor WebClient cache.
     * @return success or fail
     */
    public boolean updateToken(Long vendorId, Long tokenId, VendorTokenUpdateReq reqDto) {
        VendorToken token = new VendorToken();
        token.setId(tokenId);
        token.setVendorId(vendorId);
        token.setApiKey(reqDto.getApiKey());
        token.setTokenName(reqDto.getTokenName());
        token.setIsActive(reqDto.getIsActive());
        token.setExpiresAt(reqDto.getExpiresAt());

        return vendorTokenMapper.update(token) > 0;
    }

    /**
     * Deletes the Token by token id and refreshes the vendor WebClient cache.
     * @param id token id
     * @return success or fail
     */
    public boolean deleteToken(Long id) {
        return vendorTokenMapper.deleteById(id) > 0;
    }
}
