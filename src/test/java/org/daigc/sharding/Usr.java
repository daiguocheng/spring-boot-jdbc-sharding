package org.daigc.sharding;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "usr")
@Getter
@Setter
@EqualsAndHashCode
public class Usr {
    @Id
    private String id;
    private Long mobile;
    private String name;
    private String password;
    private Date updated;
}
