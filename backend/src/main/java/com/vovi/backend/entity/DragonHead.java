package com.vovi.backend.entity;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DragonHead extends BaseEntity {

    @Column(nullable = false)
    private Double eyesCount;

    @Column(nullable = false)
    private Double toothCount;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<ChangeHistory> changeHistory = new ArrayList<>();

    public DragonHead(Double eyesCount, Double toothCount) {
        super();
        if (eyesCount == null || toothCount == null) {
            throw new IllegalArgumentException("EyesCount and ToothCount must not be null");
        }
        this.eyesCount = eyesCount;
        this.toothCount = toothCount;
    }

    public void setUpdateFields(User updatedBy, ZonedDateTime updatedTime) {
        setUpdatedBy(updatedBy);
        setUpdatedTime(updatedTime);

        var historyItem = new ChangeHistory();
        historyItem.setChangedBy(updatedBy);
        historyItem.setChangeTime(updatedTime);
        historyItem.setEntity(this);

        changeHistory.add(historyItem);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DragonHead chapter = (DragonHead) o;
        return getId() != null && Objects.equals(getId(), chapter.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Entity
    static class ChangeHistory extends EntityChangeHistory<DragonHead> {
    }
}
