package org.daigc.sharding;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "usr_idx")
@Getter
@Setter
@NoArgsConstructor
public class UsrIdx {
    @Id
    private Long mobile;
    private String id;
    private Date created;

    public UsrIdx(Long mobile, String id) {
        this.mobile = mobile;
        this.id = id;
        this.created = new Date();
    }
}
