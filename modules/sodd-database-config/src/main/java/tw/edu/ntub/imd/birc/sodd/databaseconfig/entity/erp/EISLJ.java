package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@IdClass(EISLJPK.class)
public class EISLJ {
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
    @Column(name = "LJ001")
    private Timestamp LJ001;
    @Basic
    @Column(name = "LJ002")
    private String LJ002;
    @Basic
    @Column(name = "LJ003")
    private BigDecimal LJ003;
    @Basic
    @Column(name = "LJ004")
    private BigDecimal LJ004;
    @Basic
    @Column(name = "LJ005")
    private BigDecimal LJ005;
    @Basic
    @Column(name = "LJ006")
    private BigDecimal LJ006;
    @Basic
    @Column(name = "LJ007")
    private BigDecimal LJ007;
    @Basic
    @Column(name = "LJ008")
    private BigDecimal LJ008;
    @Basic
    @Column(name = "LJ009")
    private BigDecimal LJ009;
    @Basic
    @Column(name = "LJ010")
    private BigDecimal LJ010;
    @Basic
    @Column(name = "LJ011")
    private BigDecimal LJ011;
    @Basic
    @Column(name = "LJ012")
    private BigDecimal LJ012;
    @Basic
    @Column(name = "LJ013")
    private BigDecimal LJ013;
    @Basic
    @Column(name = "LJ014")
    private BigDecimal LJ014;
    @Basic
    @Column(name = "LJ015")
    private BigDecimal LJ015;
    @Basic
    @Column(name = "LJ016")
    private BigDecimal LJ016;
    @Basic
    @Column(name = "LJ017")
    private BigDecimal LJ017;
    @Basic
    @Column(name = "LJ018")
    private String LJ018;
    @Basic
    @Column(name = "LJ019")
    private BigDecimal LJ019;
    @Basic
    @Column(name = "LJ020")
    private BigDecimal LJ020;
    @Basic
    @Column(name = "LJ021")
    private String LJ021;
    @Basic
    @Column(name = "LJ022")
    private String LJ022;
    @Basic
    @Column(name = "LJ023")
    private String LJ023;
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
