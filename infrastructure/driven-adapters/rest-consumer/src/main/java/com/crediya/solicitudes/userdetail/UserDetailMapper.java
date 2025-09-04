package com.crediya.solicitudes.userdetail;

import com.crediya.solicitudes.model.userdetail.UserDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {
    UserDetail toDomain(UserDetailResponseDTO dto);
}
