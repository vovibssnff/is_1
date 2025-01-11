package com.vovi.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter
public class Person extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color eyeColor;

    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @Embedded
    @Valid
    private Location location = new Location();

    @Column(nullable = false, length = 38)
    private String passportId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Country must not be empty")
    private Country nationality;

    @OneToMany(mappedBy = "entity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChangeHistory> changeHistory = new ArrayList<>();

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
        Person that = (Person) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }

    @Entity
    static class ChangeHistory extends EntityChangeHistory<Person> {
    }
}
