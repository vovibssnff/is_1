package com.vovi.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DragonCaveDTO {
    private Long id;
    private Long creatorId;
    private ZonedDateTime updatedTime;
    private Float depth;
    private Double numberOfTreasures;
}
