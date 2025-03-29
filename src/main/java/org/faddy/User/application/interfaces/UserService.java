package org.faddy.User.application.interfaces;

import org.faddy.User.application.dto.CreateUserRequestDto;
import org.faddy.User.application.dto.GetUserResponseDto;
import org.faddy.User.domain.User;

public interface UserService {

    /**
     * 새 사용자를 생성합니다.
     *
     * @param dto 사용자 생성에 필요한 정보를 담은 DTO
     * @return 새로 생성된 사용자 객체
     * @throws IllegalArgumentException 사용자 생성에 실패한 경우
     */
    User createUser(CreateUserRequestDto dto);

    /**
     * ID로 사용자를 조회합니다.
     *
     * @param id 조회할 사용자의 ID
     * @return 조회된 사용자 객체
     * @throws IllegalArgumentException 해당 ID의 사용자가 없는 경우
     */
    User getUser(Long id);


    /**
     *  ID로 사용자 profile 조회 (이름, profileImage)
     *
     * @Param id 조회할 사용자의 ID
     * @return 조회된 사용자 profile을 담은 Response
     * */
    public GetUserResponseDto getUserProfile(Long id);
}