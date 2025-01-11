package com.vovi.backend.dto;

import com.vovi.backend.entity.Color;
import com.vovi.backend.entity.DragonCharacter;
import com.vovi.backend.entity.DragonType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DragonDTO {
    private Long id;
    private Long creatorId;
    private ZonedDateTime updatedTime;
    private String name;
    private Double x;
    private Integer y;
    private Long caveId;
    private Long personId;
    private Long age;
    private Color color;
    private DragonType type;
    private DragonCharacter character;
    private Long headId;
}
