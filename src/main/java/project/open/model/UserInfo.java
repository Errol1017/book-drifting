package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.operation.entity.Client;

/**
 * Created by Errol on 17/5/7.
 */
public class UserInfo {

    private String nickname;
    private String avatar;
    private String newBook = "0";
    private String newMsg = "0";

    public UserInfo(Client client, ComService comService) {
        this.nickname = client.getNickName();
        this.avatar = Base64Util.img2String(comService.getFileBathPath(), client.getAvatar());
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNewBook() {
        return newBook;
    }

    public void setNewBook(String newBook) {
        this.newBook = newBook;
    }

    public String getNewMsg() {
        return newMsg;
    }

    public void setNewMsg(String newMsg) {
        this.newMsg = newMsg;
    }
}
