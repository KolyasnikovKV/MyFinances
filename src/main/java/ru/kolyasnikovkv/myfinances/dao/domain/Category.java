package ru.kolyasnikovkv.myfinances.dao.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Category extends AbstractEntity{
    private String description;
}
