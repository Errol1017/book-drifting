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

    public ClientList(Client client, CacheManager cacheManager) {
        this.id = String.valueOf(client.getId());
        this.name = client.getName();
        this.mobile = client.getMobile();
        this.agency = cacheManager.getAgencyCache(client.getAgencyId()).getName();
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
}
