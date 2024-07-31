package org.jang.global.entities;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@Getter @Setter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseMemberEntitiy extends BaseEntity {
    @CreatedBy
    @Column(length = 65, nullable = false)
    private String createdBy;

    @LastModifiedBy
    @Column(length = 65, insertable = false)
    private String modifiedBy;

}
