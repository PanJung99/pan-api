package io.github.panjung99.panapi.user.dao;

import io.github.panjung99.panapi.user.entity.ApiKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiKeyMapper {

    /**
     * Insert a new Api Key record
     * @param apiKey new Api Key entity
     * @return the number of rows inserted
     */
    int insert(ApiKey apiKey);

    /**
     * Soft delete Api Key record by marking "deleted" value.
     * @param id the Api Key id
     * @param userId the user id, ensure the Api Key belongs to the user.
     * @return the number of rows deleted
     */
    int softDelete(@Param("id") Long id, @Param("userId") Long userId);

    /**
     * Update an existing Api Key record by id
     * @param apiKey the Api Key entity with updated values.
     * @return the number of rows updated
     */
    int updateById(ApiKey apiKey);

    ApiKey selectById(@Param("id") Long id);

    ApiKey selectByApiKey(@Param("apiKey") String apiKey);

    List<ApiKey> selectByUserId(@Param("userId") Long userId);

    List<ApiKey> selectActiveKeysByUserId(@Param("userId") Long userId);

}