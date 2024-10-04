package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@Entity
public class EISLHId implements Serializable {
    @Id
    @Column(name = "COMPANY", length = 20)
    private String company;
    @Id
    @Column(name = "CREATOR", length = 10)
    private String creator;
    @Id
    @Column(name = "USR_GROUP", length = 10)
    private String usrGroup;
    @Id
    @Column(name = "CREATE_DATE", length = 8)
    private String createDate;
}
