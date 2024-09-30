package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@IdClass(EISLEId.class)
@Table(name = "EISLE")
public class EISLE {
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
    @Column(name = "MODIFIER", length = 10)
    private String modifier;
    @Column(name = "MODI_DATE", length = 8)
    private String modiDate;
    @Column(name = "FLAG")
    private BigDecimal flag;
    @Column(name = "LE001", length = 14)
    private String le001;
    @Column(name = "LE002", length = 14)
    private String le002;
    @Column(name = "LE003", length = 14)
    private String le003;
    @Column(name = "LE004", length = 14)
    private String le004;
    @Column(name = "LE005", length = 40)
    private String le005;
    @Column(name = "LE006", length = 8)
    private String le006;
    @Column(name = "LE007", length = 10)
    private String le007;
    @Column(name = "LE008", length = 12)
    private String le008;
    @Column(name = "LE009", length = 12)
    private String le009;
    @Column(name = "LE010")
    private Timestamp le010;
    @Column(name = "LE011")
    private BigDecimal le011;
    @Column(name = "LE012")
    private BigDecimal le012;
    @Column(name = "LE013")
    private BigDecimal le013;
    @Column(name = "LE014", length = 40)
    private String le014;
    @Column(name = "LE015", length = 40)
    private String le015;
    @Column(name = "LE016", length = 40)
    private String le016;
    @Column(name = "LE017", length = 40)
    private String le017;
    @Column(name = "LE018", length = 150)
    private String le018;
    @Column(name = "LE019", length = 40)
    private String le019;
    @Column(name = "LE020", length = 40)
    private String le020;
    @Column(name = "LE021", length = 40)
    private String le021;
    @Column(name = "LE022", length = 40)
    private String le022;
    @Column(name = "LE023")
    private BigDecimal le023;
    @Column(name = "LE024")
    private BigDecimal le024;
    @Column(name = "LE025", length = 1)
    private String le025;
    @Column(name = "LE026", length = 30)
    private String le026;
    @Column(name = "LE027", length = 60)
    private String le027;
    @Column(name = "UDF01", length = 255)
    private String udf01;
    @Column(name = "UDF02", length = 255)
    private String udf02;
    @Column(name = "UDF03", length = 255)
    private String udf03;
    @Column(name = "UDF04", length = 255)
    private String udf04;
    @Column(name = "UDF05", length = 255)
    private String udf05;
    @Column(name = "UDF06")
    private BigDecimal udf06;
    @Column(name = "UDF07")
    private BigDecimal udf07;
    @Column(name = "UDF08")
    private BigDecimal udf08;
    @Column(name = "UDF09")
    private BigDecimal udf09;
    @Column(name = "UDF10")
    private BigDecimal udf10;
}
