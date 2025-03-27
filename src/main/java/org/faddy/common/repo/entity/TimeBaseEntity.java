package org.faddy.common.repo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

@EntityListeners(AbstractMethodError.class)
@MappedSuperclass
@Getter
public class TimeBaseEntity {
    @CreatedDate
    @Column(name = "reg_dt", updatable = false)
    private LocalDateTime regDt;

    @LastModifiedBy
    @Column(name = "mod_dt")
    private LocalDateTime modDt;
}
