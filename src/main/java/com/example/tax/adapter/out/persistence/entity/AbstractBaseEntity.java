package com.example.tax.adapter.out.persistence.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.hibernate.Hibernate;

import java.util.Objects;

@MappedSuperclass
@Getter
public abstract class AbstractBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long srl;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractBaseEntity that = (AbstractBaseEntity) o;
        return srl != null && Objects.equals(srl, that.srl);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
