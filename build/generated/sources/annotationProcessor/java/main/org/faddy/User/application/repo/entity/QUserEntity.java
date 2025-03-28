package org.faddy.User.application.repo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserEntity is a Querydsl query type for UserEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserEntity extends EntityPathBase<UserEntity> {

    private static final long serialVersionUID = 1810879556L;

    public static final QUserEntity userEntity = new QUserEntity("userEntity");

    public final org.faddy.common.infrastructure.entity.QTimeBaseEntity _super = new org.faddy.common.infrastructure.entity.QTimeBaseEntity(this);

    public final NumberPath<Integer> followerCount = createNumber("followerCount", Integer.class);

    public final NumberPath<Integer> followingCount = createNumber("followingCount", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modDt = _super.modDt;

    public final StringPath name = createString("name");

    public final StringPath profileImageUrl = createString("profileImageUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public QUserEntity(String variable) {
        super(UserEntity.class, forVariable(variable));
    }

    public QUserEntity(Path<? extends UserEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserEntity(PathMetadata metadata) {
        super(UserEntity.class, metadata);
    }

}

