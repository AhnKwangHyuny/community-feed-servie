package org.faddy.User.application.repo.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRelationIdEntity is a Querydsl query type for UserRelationIdEntity
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserRelationIdEntity extends BeanPath<UserRelationIdEntity> {

    private static final long serialVersionUID = 1589488155L;

    public static final QUserRelationIdEntity userRelationIdEntity = new QUserRelationIdEntity("userRelationIdEntity");

    public final NumberPath<Long> followerId = createNumber("followerId", Long.class);

    public final NumberPath<Long> followingId = createNumber("followingId", Long.class);

    public QUserRelationIdEntity(String variable) {
        super(UserRelationIdEntity.class, forVariable(variable));
    }

    public QUserRelationIdEntity(Path<? extends UserRelationIdEntity> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRelationIdEntity(PathMetadata metadata) {
        super(UserRelationIdEntity.class, metadata);
    }

}

