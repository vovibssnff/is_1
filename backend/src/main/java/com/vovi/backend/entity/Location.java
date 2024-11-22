package com.vovi.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class Location {

    @Column(nullable = false)
    private Double x;

    @Column(nullable = false)
    private Float y;
}
