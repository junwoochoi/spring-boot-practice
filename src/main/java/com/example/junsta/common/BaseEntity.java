package com.example.junsta.common;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CreatedAt",updatable = false, nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "ModifiedAt", nullable = false)
    @UpdateTimestamp
    private LocalDateTime modifiedAt;
}
