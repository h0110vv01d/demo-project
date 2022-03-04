package org.h0110w.som.core.service.mapper;

import java.util.List;

public interface AbstractMapper<ENTITY, DTO> {
    DTO toDto(ENTITY entity);

    ENTITY toEntity(DTO dto);

    List<DTO> toDtos(List<ENTITY> entities);

    List<ENTITY> toEntities(List<DTO> dtos);
}
