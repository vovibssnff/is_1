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
public class DragonHeadDTO {
    private Long id;
    private Long creatorId;
    private ZonedDateTime updatedTime;
    private Double eyesCount;
    private Double toothCount;
}
