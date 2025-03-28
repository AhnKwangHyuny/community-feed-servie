package org.faddy.User.application.repo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRelationEntity is a Querydsl query type for UserRelationEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRelationEntity extends EntityPathBase<UserRelationEntity> {

    private static final long serialVersionUID = 1229610080L;

    public static final QUserRelationEntity userRelationEntity = new QUserRelationEntity("userRelationEntity");

    public final org.faddy.common.infrastructure.entity.QTimeBaseEntity _super = new org.faddy.common.infrastructure.entity.QTimeBaseEntity(this);

    public final NumberPath<Long> followerId = createNumber("followerId", Long.class);

    public final NumberPath<Long> followingId = createNumber("followingId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDt = _super.modDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public QUserRelationEntity(String variable) {
        super(UserRelationEntity.class, forVariable(variable));
    }

    public QUserRelationEntity(Path<? extends UserRelationEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRelationEntity(PathMetadata metadata) {
        super(UserRelationEntity.class, metadata);
    }

}

