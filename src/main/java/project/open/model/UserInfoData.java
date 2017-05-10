package project.open.model;

import common.CRUD.service.ComService;
import common.Util.Base64Util;
import project.navigator.service.CacheManager;
import project.operation.entity.Client;

/**
 * Created by Errol on 17/5/8.
 */
public class UserInfoData {

    private String avatar;
    private String nickname;
    private String name;
    private String gender;
    private String mobile;
    private String idNum;
    private String agency;
    private String stacks;

    public UserInfoData(Client client, ComService comService, CacheManager cacheManager) {
        this.avatar = Base64Util.img2String(comService.getFileBathPath(), client.getAvatar());
        this.nickname = client.getNickName();
        this.name = client.getName();
        this.gender = client.getGender().getName();
        this.mobile = client.getMobile();
        this.idNum = client.getIdentityNumber();
        this.agency = cacheManager.getAgency(client.getAgencyId()).getName();
        this.stacks = "";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getIdNum() {
        return idNum;
    }

    public void setIdNum(String idNum) {
        this.idNum = idNum;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getStacks() {
        return stacks;
    }

    public void setStacks(String stacks) {
        this.stacks = stacks;
    }
}
