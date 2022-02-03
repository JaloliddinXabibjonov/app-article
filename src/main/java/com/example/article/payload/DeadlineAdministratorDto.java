package com.example.article.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeadlineAdministratorDto {

    private Integer id;
    private UUID administratorId;
    private Integer deadline;

}
