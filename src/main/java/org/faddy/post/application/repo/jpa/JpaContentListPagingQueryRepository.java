package org.faddy.post.application.repo.jpa;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.User.infrastructure.repo.entity.QUserRelationEntity;
import org.faddy.post.application.dto.GetContentResponseDto;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class JpaContentListPagingQueryRepository {

    private final JPAQueryFactory queryFactory;
    private static final QUserRelationEntity relationEntity = QUserRelationEntity.userRelationEntity;

    public List<GetContentResponseDto> getContentResponse(Long userId, Long lastContentId) {

        return null;
    }
}
