package project.basic.model;

import project.basic.entity.Agency;
import project.operation.entity.Stacks;

/**
 * Created by Errol on 17/5/17.
 */
public class AgencyCache {

    private int id;
    private String name;
    private String code;
    private long stackId;
    private Stacks stack;

    public AgencyCache(Object[] o) {
        Agency agency = (Agency)o[0];
        this.id = agency.getId();
        this.name = agency.getName();
        this.code = agency.getCode();
        this.stackId = agency.getStackId();
        this.stack = (Stacks)o[1];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getStackId() {
        return stackId;
    }

    public void setStackId(long stackId) {
        this.stackId = stackId;
    }

    public Stacks getStack() {
        return stack;
    }

    public void setStack(Stacks stack) {
        this.stack = stack;
    }
}
