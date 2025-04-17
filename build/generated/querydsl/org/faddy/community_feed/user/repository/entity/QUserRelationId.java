package org.faddy.community_feed.user.repository.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserRelationId is a Querydsl query type for UserRelationId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserRelationId extends BeanPath<UserRelationId> {

    private static final long serialVersionUID = -1098668116L;

    public static final QUserRelationId userRelationId = new QUserRelationId("userRelationId");

    public final NumberPath<Long> followerUserId = createNumber("followerUserId", Long.class);

    public final NumberPath<Long> followingUserId = createNumber("followingUserId", Long.class);

    public QUserRelationId(String variable) {
        super(UserRelationId.class, forVariable(variable));
    }

    public QUserRelationId(Path<? extends UserRelationId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserRelationId(PathMetadata metadata) {
        super(UserRelationId.class, metadata);
    }

}

