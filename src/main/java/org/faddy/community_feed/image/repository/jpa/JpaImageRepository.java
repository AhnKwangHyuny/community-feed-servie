package org.faddy.community_feed.image.repository.jpa;

import java.util.List;
import org.faddy.community_feed.image.repository.entity.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaImageRepository extends JpaRepository<ImageEntity, Long> {

    @Modifying
    @Query("DELETE FROM ImageEntity i WHERE i.id IN :ids")
    void deleteAllByIdIn(@Param("ids") List<Long> ids);
}