package org.faddy.community_feed.post.repository.entity.image;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPostThumbnailEntity is a Querydsl query type for PostThumbnailEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostThumbnailEntity extends EntityPathBase<PostThumbnailEntity> {

    private static final long serialVersionUID = -411392025L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPostThumbnailEntity postThumbnailEntity = new QPostThumbnailEntity("postThumbnailEntity");

    public final org.faddy.community_feed.common.repository.QTimeBaseEntity _super = new org.faddy.community_feed.common.repository.QTimeBaseEntity(this);

    public final NumberPath<Integer> displayOrder = createNumber("displayOrder", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.faddy.community_feed.image.repository.entity.QImageEntity image;

    public final BooleanPath isMain = createBoolean("isMain");

    public final org.faddy.community_feed.post.repository.entity.post.QPostEntity post;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    public QPostThumbnailEntity(String variable) {
        this(PostThumbnailEntity.class, forVariable(variable), INITS);
    }

    public QPostThumbnailEntity(Path<? extends PostThumbnailEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPostThumbnailEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPostThumbnailEntity(PathMetadata metadata, PathInits inits) {
        this(PostThumbnailEntity.class, metadata, inits);
    }

    public QPostThumbnailEntity(Class<? extends PostThumbnailEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.image = inits.isInitialized("image") ? new org.faddy.community_feed.image.repository.entity.QImageEntity(forProperty("image")) : null;
        this.post = inits.isInitialized("post") ? new org.faddy.community_feed.post.repository.entity.post.QPostEntity(forProperty("post"), inits.get("post")) : null;
    }

}

