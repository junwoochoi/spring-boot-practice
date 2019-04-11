package com.example.junsta.common;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CreatedAt",updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "ModifiedAt")
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
