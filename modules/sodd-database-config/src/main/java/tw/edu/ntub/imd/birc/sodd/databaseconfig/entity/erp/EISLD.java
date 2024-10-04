package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@IdClass(EISLDId.class)
public class EISLD {
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
    @Column(name = "LD001")
    private String LD001;
    @Basic
    @Column(name = "LD002")
    private String LD002;
    @Basic
    @Column(name = "LD003")
    private String LD003;
    @Basic
    @Column(name = "LD004")
    private String LD004;
    @Basic
    @Column(name = "LD005")
    private String LD005;
    @Basic
    @Column(name = "LD006")
    private String LD006;
    @Basic
    @Column(name = "LD007")
    private String LD007;
    @Basic
    @Column(name = "LD008")
    private String LD008;
    @Basic
    @Column(name = "LD009")
    private String LD009;
    @Basic
    @Column(name = "LD010")
    private String LD010;
    @Basic
    @Column(name = "LD011")
    private String LD011;
    @Basic
    @Column(name = "LD012")
    private Timestamp LD012;
    @Basic
    @Column(name = "LD013")
    private BigDecimal LD013;
    @Basic
    @Column(name = "LD014")
    private BigDecimal LD014;
    @Basic
    @Column(name = "LD015")
    private BigDecimal LD015;
    @Basic
    @Column(name = "LD016")
    private BigDecimal LD016;
    @Basic
    @Column(name = "LD017")
    private BigDecimal LD017;
    @Basic
    @Column(name = "LD018")
    private BigDecimal LD018;
    @Basic
    @Column(name = "LD019")
    private BigDecimal LD019;
    @Basic
    @Column(name = "LD020")
    private BigDecimal LD020;
    @Basic
    @Column(name = "LD021")
    private BigDecimal LD021;
    @Basic
    @Column(name = "LD022")
    private BigDecimal LD022;
    @Basic
    @Column(name = "LD023")
    private BigDecimal LD023;
    @Basic
    @Column(name = "LD024")
    private String LD024;
    @Basic
    @Column(name = "LD025")
    private String LD025;
    @Basic
    @Column(name = "LD026")
    private String LD026;
    @Basic
    @Column(name = "LD027")
    private String LD027;
    @Basic
    @Column(name = "LD028")
    private String LD028;
    @Basic
    @Column(name = "LD029")
    private String LD029;
    @Basic
    @Column(name = "LD030")
    private String LD030;
    @Basic
    @Column(name = "LD031")
    private String LD031;
    @Basic
    @Column(name = "LD032")
    private String LD032;
    @Basic
    @Column(name = "LD033")
    private String LD033;
    @Basic
    @Column(name = "LD034")
    private String LD034;
    @Basic
    @Column(name = "LD035")
    private BigDecimal LD035;
    @Basic
    @Column(name = "LD036")
    private BigDecimal LD036;
    @Basic
    @Column(name = "LD037")
    private String LD037;
    @Basic
    @Column(name = "LD038")
    private String LD038;
    @Basic
    @Column(name = "LD039")
    private String LD039;
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
