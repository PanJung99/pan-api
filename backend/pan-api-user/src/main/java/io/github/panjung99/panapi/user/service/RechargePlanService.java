package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.user.dao.RechargePlanMapper;
import io.github.panjung99.panapi.user.entity.RechargePlan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RechargePlanService {

    private final RechargePlanMapper rechargePlanMapper;

    public RechargePlan getById(Long id) {
        return rechargePlanMapper.findById(id);
    }

    public List<RechargePlan> listActivePlans() {
        return rechargePlanMapper.findAllActive();
    }

    public void create(RechargePlan plan) {
        rechargePlanMapper.insert(plan);
    }

    public void update(RechargePlan plan) {
        rechargePlanMapper.update(plan);
    }

    public void delete(Long id) {
        rechargePlanMapper.delete(id);
    }
}
