package org.daigc.sharding;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "usr")
@Data
public class Usr {
    @Id
    private String id;
    private Long mobile;
    private String name;
    private String password;
    private Date updated;
}
