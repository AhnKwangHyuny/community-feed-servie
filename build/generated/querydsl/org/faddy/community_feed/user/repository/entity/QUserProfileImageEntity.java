package org.faddy.community_feed.user.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserProfileImageEntity is a Querydsl query type for UserProfileImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserProfileImageEntity extends EntityPathBase<UserProfileImageEntity> {

    private static final long serialVersionUID = -866295254L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserProfileImageEntity userProfileImageEntity = new QUserProfileImageEntity("userProfileImageEntity");

    public final org.faddy.community_feed.common.repository.QTimeBaseEntity _super = new org.faddy.community_feed.common.repository.QTimeBaseEntity(this);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.faddy.community_feed.image.repository.entity.QImageEntity image;

    public final BooleanPath isActive = createBoolean("isActive");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    public final QUserEntity user;

    public QUserProfileImageEntity(String variable) {
        this(UserProfileImageEntity.class, forVariable(variable), INITS);
    }

    public QUserProfileImageEntity(Path<? extends UserProfileImageEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserProfileImageEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserProfileImageEntity(PathMetadata metadata, PathInits inits) {
        this(UserProfileImageEntity.class, metadata, inits);
    }

    public QUserProfileImageEntity(Class<? extends UserProfileImageEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new org.faddy.community_feed.image.repository.entity.QImageEntity(forProperty("image")) : null;
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

