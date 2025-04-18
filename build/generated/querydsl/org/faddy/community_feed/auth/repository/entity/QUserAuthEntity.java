package org.faddy.community_feed.auth.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAuthEntity is a Querydsl query type for UserAuthEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAuthEntity extends EntityPathBase<UserAuthEntity> {

    private static final long serialVersionUID = 774602979L;

    public static final QUserAuthEntity userAuthEntity = new QUserAuthEntity("userAuthEntity");

    public final StringPath email = createString("email");

    public final DateTimePath<java.time.LocalDateTime> lastLoginAt = createDateTime("lastLoginAt", java.time.LocalDateTime.class);

    public final StringPath password = createString("password");

    public final StringPath role = createString("role");

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserAuthEntity(String variable) {
        super(UserAuthEntity.class, forVariable(variable));
    }

    public QUserAuthEntity(Path<? extends UserAuthEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAuthEntity(PathMetadata metadata) {
        super(UserAuthEntity.class, metadata);
    }

}

