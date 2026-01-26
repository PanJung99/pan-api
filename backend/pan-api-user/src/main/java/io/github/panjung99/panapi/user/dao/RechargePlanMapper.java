package io.github.panjung99.panapi.user.dao;

import io.github.panjung99.panapi.user.entity.RechargePlan;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RechargePlanMapper {

    RechargePlan findById(Long id);

    List<RechargePlan> findAllActive();

    int insert(RechargePlan plan);

    int update(RechargePlan plan);

    int delete(Long id);
}
