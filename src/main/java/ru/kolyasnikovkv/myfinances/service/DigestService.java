package ru.kolyasnikovkv.myfinances.service;

import org.apache.commons.codec.digest.DigestUtils;

public class DigestService {

    public String hash(String str) {
        return DigestUtils.md5Hex(str);
    }
}
