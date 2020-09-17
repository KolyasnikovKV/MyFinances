package ru.kolyasnikovkv.myfinances.dao.domain;


import java.io.Serializable;

public class AbstractEntity implements Serializable {
    protected Long id;

    public Long getId(){return id;}

    public void setId(Long id) {this.id = id;}
}
