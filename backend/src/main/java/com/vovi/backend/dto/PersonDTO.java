package com.vovi.backend.dto;

import com.vovi.backend.entity.Color;
import com.vovi.backend.entity.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private Long id;
    private Long creatorId;
    private ZonedDateTime updatedTime;
    private String name;
    private Color eyeColor;
    private Color hairColor;
    private Double x;
    private Float y;
    private String passportId;
    private Country nationality;
}
