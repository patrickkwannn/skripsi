package com.skripsi.skripsiservice.model;
import com.skripsi.skripsiservice.generator.StringSequenceIdentifier;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "VERIFIED_CODE")
public class VerifiedNumber {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "code_seq")
    @GenericGenerator(name="code_seq",strategy = "com.skripsi.skripsiservice.generator.StringSequenceIdentifier",parameters = {
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.INCREMENT_PARAM, value = "50"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.VALUE_PREFIX_PARAMETER, value = "CD"),
            @org.hibernate.annotations.Parameter(name = StringSequenceIdentifier.NUMBER_FORMAT_PARAMETER, value = "%05d")
    })
    @Column(name = "CODE_ID")
    private String codeId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private UserTable user;

    @Column(name = "VERIFY_CODE")
    private String code;

    @Column(name = "COUNTER")
    private int counter;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public UserTable getUser() {
        return user;
    }

    public void setUser(UserTable user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
