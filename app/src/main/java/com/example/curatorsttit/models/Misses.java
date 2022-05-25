package com.example.curatorsttit.models;

import java.util.Date;

public class Misses {
    public int id;
    public Date IllnessDate;
    public Date RecoveryDate;

    public Misses(Date illnessDate, Date recoveryDate) {
        IllnessDate = illnessDate;
        RecoveryDate = recoveryDate;
    }

    public Misses() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getIllnessDate() {
        return IllnessDate;
    }

    public void setIllnessDate(Date illnessDate) {
        IllnessDate = illnessDate;
    }

    public Date getRecoveryDate() {
        return RecoveryDate;
    }

    public void setRecoveryDate(Date recoveryDate) {
        RecoveryDate = recoveryDate;
    }
}
