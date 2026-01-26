package io.github.panjung99.panapi.user.service;

import io.github.panjung99.panapi.user.dao.RechargePlanMapper;
import io.github.panjung99.panapi.user.entity.RechargePlan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RechargePlanService {

    @Autowired
    private RechargePlanMapper rechargePlanMapper;

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
