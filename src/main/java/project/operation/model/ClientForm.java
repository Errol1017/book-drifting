package project.operation.model;

import project.operation.entity.Client;

/**
 * Created by Errol on 17/5/1.
 */
public class ClientForm {

    private String id;
    private String name;
    private String mobile;
    private String identityNumber;
    private String agencyId;
    private String individualIds;

    public ClientForm() {
    }

    public ClientForm(Client client) {
        this.id = String.valueOf(client.getId());
        this.name = client.getName();
        this.mobile = client.getMobile();
        this.identityNumber = client.getIdentityNumber();
        this.agencyId = String.valueOf(client.getAgencyId());
        this.individualIds = client.getIndividualIds();
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

    public String getIdentityNumber() {
        return identityNumber;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public String getIndividualIds() {
        return individualIds;
    }
}
