package project.apiservice.infrastructure.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import project.apiservice.domain.model.UserEntity;
import project.apiservice.openapi.model.UserRegistrationRequest;
import project.apiservice.openapi.model.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "role", expression = "java(project.apiservice.domain.enums.UserRole.EMPLOYEE)")
    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    UserEntity userRequestToUserEntity(UserRegistrationRequest request);

    UserResponse userEntityToUserResponse(UserEntity entity);

    List<UserResponse> userEntityListToResponseList(List<UserEntity> list);
}
