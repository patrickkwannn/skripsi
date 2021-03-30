package com.skripsi.skripsiservice.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.skripsi.skripsiservice.model.PostApplication;

import java.util.Date;

public class AllPostApplicationDomain extends PostApplication {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy mm:ss")
    private Date lastReply;

    public Date getLastReply() {
        return lastReply;
    }

    public void setLastReply(Date lastReply) {
        this.lastReply = lastReply;
    }
}
//@startuml
//@enduml