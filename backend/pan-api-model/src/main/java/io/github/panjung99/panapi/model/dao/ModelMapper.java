package io.github.panjung99.panapi.model.dao;

import io.github.panjung99.panapi.model.entity.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ModelMapper {

    /**
     * 根据ID查询标签
     * @param id 标签ID
     * @return 标签实体
     */
    Model selectById(@Param("id") Long id);

    /**
     * 根据标签名称查询
     * @param name 标签名称
     * @return 标签实体
     */
    Model selectByName(@Param("name") String name);

    /**
     * 根据标签名，查询上线模型
     * @param name 标签名称
     * @return 标签实体
     */
    Model selectActiveByName(@Param("name") String name);

    /**
     * 查询激活的供API使用的模型
     * @return 标签列表
     */
    List<Model> selectApiActive();

    /**
     * 查询所有供API使用的模型
     * @return 标签列表
     */
    List<Model> selectApiAll();


    /**
     * 插入模型信息
     * @param model 模型实体
     * @return 影响行数
     */
    int insert(Model model);

    /**
     * 更新模型信息
     * @param model 模型实体
     * @return 影响行数
     */
    int update(Model model);

    /**
     * 逻辑删除
     * @param id 标签ID
     * @return 影响行数
     */
    int logicalDelete(@Param("id") Long id);

}