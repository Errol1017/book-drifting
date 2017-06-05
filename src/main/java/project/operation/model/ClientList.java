package project.operation.model;

import project.navigator.service.CacheManager;
import project.operation.entity.Client;

/**
 * Created by Errol on 17/5/1.
 */
public class ClientList {

    private String id;
    private String name;
    private String mobile;
    private String agency;
    private String sum;
    private String isAdmin;
    private String idNum;

    public ClientList(Client client, CacheManager cacheManager) {
        this.id = String.valueOf(client.getId());
        this.name = client.getNickName() + "（" + client.getName() + "）";
        this.mobile = client.getMobile();
        this.agency = cacheManager.getAgencyCache(client.getAgencyId()).getName();
        this.sum = client.getBorrowingSum()==0?"":String.valueOf(client.getBorrowingSum());
        this.isAdmin = client.isAdmin()?"图书管理员":"";
        this.idNum = client.getIdentityNumber();
    }

    public String getId() {
        return id;
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

    public String getSum() {
        return sum;
    }

    public String getIdNum() {
        return idNum;
    }

    public String getIsAdmin() {
        return isAdmin;
    }
}
