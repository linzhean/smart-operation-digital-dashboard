package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@IdClass(EISLGId.class)
public class EISLG {
    @Id
    @Column(name = "COMPANY")
    private String company;
    @Id
    @Column(name = "CREATOR")
    private String creator;
    @Id
    @Column(name = "USR_GROUP")
    private String usrGroup;
    @Id
    @Column(name = "CREATE_DATE")
    private String createDate;
    @Basic
    @Column(name = "MODIFIER")
    private String modifier;
    @Basic
    @Column(name = "MODI_DATE")
    private String modiDate;
    @Basic
    @Column(name = "FLAG")
    private BigDecimal flag;
    @Basic
    @Column(name = "LG001")
    private String LG001;
    @Basic
    @Column(name = "LG002")
    private String LG002;
    @Basic
    @Column(name = "LG003")
    private String LG003;
    @Basic
    @Column(name = "LG004")
    private String LG004;
    @Basic
    @Column(name = "LG005")
    private String LG005;
    @Basic
    @Column(name = "LG006")
    private Timestamp LG006;
    @Basic
    @Column(name = "LG007")
    private BigDecimal LG007;
    @Basic
    @Column(name = "LG008")
    private BigDecimal LG008;
    @Basic
    @Column(name = "LG009")
    private BigDecimal LG009;
    @Basic
    @Column(name = "LG010")
    private BigDecimal LG010;
    @Basic
    @Column(name = "LG011")
    private BigDecimal LG011;
    @Basic
    @Column(name = "LG012")
    private BigDecimal LG012;
    @Basic
    @Column(name = "LG013")
    private BigDecimal LG013;
    @Basic
    @Column(name = "LG014")
    private BigDecimal LG014;
    @Basic
    @Column(name = "LG015")
    private BigDecimal LG015;
    @Basic
    @Column(name = "LG016")
    private String LG016;
    @Basic
    @Column(name = "LG017")
    private String LG017;
    @Basic
    @Column(name = "LG018")
    private String LG018;
    @Basic
    @Column(name = "LG019")
    private String LG019;
    @Basic
    @Column(name = "LG020")
    private String LG020;
    @Basic
    @Column(name = "LG021")
    private BigDecimal LG021;
    @Basic
    @Column(name = "LG022")
    private BigDecimal LG022;
    @Basic
    @Column(name = "LG023")
    private String LG023;
    @Basic
    @Column(name = "LG024")
    private String LG024;
    @Basic
    @Column(name = "LG025")
    private String LG025;
    @Basic
    @Column(name = "UDF01")
    private String UDF01;
    @Basic
    @Column(name = "UDF02")
    private String UDF02;
    @Basic
    @Column(name = "UDF03")
    private String UDF03;
    @Basic
    @Column(name = "UDF04")
    private String UDF04;
    @Basic
    @Column(name = "UDF05")
    private String UDF05;
    @Basic
    @Column(name = "UDF06")
    private BigDecimal UDF06;
    @Basic
    @Column(name = "UDF07")
    private BigDecimal UDF07;
    @Basic
    @Column(name = "UDF08")
    private BigDecimal UDF08;
    @Basic
    @Column(name = "UDF09")
    private BigDecimal UDF09;
    @Basic
    @Column(name = "UDF10")
    private BigDecimal UDF10;
}
