package org.faddy.domain;

public class UserInfo {
    private final String name;
    private final String profileImageUrl;

    public UserInfo(String name, String profileImageUrl) {

        if(name == null || name.isEmpty()) {
            throw new IllegalArgumentException("유저 이름이 공백 또는 오류가 존재합니다.");
        }

        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }
}
