package project.basic.model;

import project.navigator.service.CacheManager;

/**
 * Created by Errol on 17/4/26.
 */
public class InvitationList {

    private String code;
    private String name = "";
    private String mobile = "";
    private String agency = "";

    public InvitationList(Object[] o, CacheManager cacheManager) {
        this.code = String.valueOf(o[0]);
        if (o[1] != null) {
            this.name = String.valueOf(o[1]);
            this.mobile = String.valueOf(o[2]);
            this.agency = cacheManager.getAgencyCache(Integer.parseInt(String.valueOf(o[3]))).getName();
        }
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getAgency() {
        return agency;
    }
}
