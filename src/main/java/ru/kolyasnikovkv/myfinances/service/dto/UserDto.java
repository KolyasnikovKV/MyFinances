package ru.kolyasnikovkv.myfinances.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id = -1L;
    private String mail;
    private String password;
    private String nick;
    private String fullName;

    @Override
    public String toString() {
        return "id = " + this.getId() + " mail = " + this.getMail() + " password = " + this.getPassword();
    }
}
