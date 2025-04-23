package org.faddy.community_feed.image.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QImageEntity is a Querydsl query type for ImageEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QImageEntity extends EntityPathBase<ImageEntity> {

    private static final long serialVersionUID = 54841572L;

    public static final QImageEntity imageEntity = new QImageEntity("imageEntity");

    public final org.faddy.community_feed.common.repository.QTimeBaseEntity _super = new org.faddy.community_feed.common.repository.QTimeBaseEntity(this);

    public final StringPath contentType = createString("contentType");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFilename = createString("originalFilename");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> regDt = _super.regDt;

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final EnumPath<org.faddy.community_feed.image.domain.ImageType> type = createEnum("type", org.faddy.community_feed.image.domain.ImageType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updDt = _super.updDt;

    public final StringPath url = createString("url");

    public QImageEntity(String variable) {
        super(ImageEntity.class, forVariable(variable));
    }

    public QImageEntity(Path<? extends ImageEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QImageEntity(PathMetadata metadata) {
        super(ImageEntity.class, metadata);
    }

}

