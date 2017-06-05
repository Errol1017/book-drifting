package project.open.model;

import common.Util.Base64Util;
import project.navigator.service.CacheManager;
import project.operation.entity.Client;
import project.resource.properties.ServerProperties;

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
    private String agencyId;
    private String stacks;

    public UserInfoData(Client client, CacheManager cacheManager) {
        this.avatar = Base64Util.img2String(ServerProperties.getInstance().getFileBasePath(), client.getAvatar());
        this.nickname = client.getNickName();
        this.name = client.getName();
        this.gender = client.getGender().getName();
        this.mobile = client.getMobile();
        this.idNum = client.getIdentityNumber();
        this.agency = cacheManager.getAgencyCache(client.getAgencyId()).getName();
        this.agencyId = String.valueOf(client.getAgencyId());
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

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
        this.agencyId = agencyId;
    }

    public String getStacks() {
        return stacks;
    }

    public void setStacks(String stacks) {
        this.stacks = stacks;
    }
}
