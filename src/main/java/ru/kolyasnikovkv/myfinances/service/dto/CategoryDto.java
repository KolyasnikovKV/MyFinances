package ru.kolyasnikovkv.myfinances.service.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryDto {

    private Long id = -1L;
    private String description;

}
