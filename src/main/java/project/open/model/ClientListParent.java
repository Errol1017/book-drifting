package project.open.model;

import project.operation.model.ClientCache;

/**
 * Created by Errol on 17/5/23.
 */
public class ClientListParent {

    private String avatar;
    private String nickname;

    public ClientListParent(ClientCache clientCache) {
        this.avatar = clientCache.getAvatar();
        this.nickname = clientCache.getNickName();
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
