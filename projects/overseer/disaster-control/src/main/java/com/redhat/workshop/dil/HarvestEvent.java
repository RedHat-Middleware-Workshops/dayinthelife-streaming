
package com.redhat.workshop.dil;

public class HarvestEvent {
    int farmid;
    int batchcnt;
    long batchtime;

    public int getFarmid() {
        return this.farmid;
    }

    public void setFarmid(final int farmid) {
        this.farmid = farmid;
    }

    public int getBatchcnt() {
        return this.batchcnt;
    }

    public void setBatchcnt(final int batchcnt) {
        this.batchcnt = batchcnt;
    }

    public long getBatchtime() {
        return this.batchtime;
    }

    public void setBatchtime(final long batchtime) {
        this.batchtime = batchtime;
    }

    @Override
    public String toString(){

        return "farmid:["+farmid+"] batchcnt:["+batchcnt+"] batchtime:["+batchtime+"]";
    }


}
