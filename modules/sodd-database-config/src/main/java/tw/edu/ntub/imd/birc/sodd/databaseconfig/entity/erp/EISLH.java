package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "EISLH")
@IdClass(EISLHId.class)
public class EISLH {
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
    @Column(name = "MODIFIER")
    private String modifier;
    @Column(name = "MODI_DATE")
    private String modiDate;
    @Column(name = "FLAG")
    private BigDecimal flag;
    @Column(name = "LH001")
    private String LH001;
    @Column(name = "LH002")
    private String LH002;
    @Column(name = "LH003")
    private String LH003;
    @Column(name = "LH004")
    private String LH004;
    @Column(name = "LH005")
    private String LH005;
    @Column(name = "LH006")
    private String LH006;
    @Column(name = "LH007")
    private String LH007;
    @Column(name = "LH008")
    private String LH008;
    @Column(name = "LH009")
    private String LH009;
    @Column(name = "LH010")
    private String LH010;
    @Column(name = "LH011")
    private String LH011;
    @Column(name = "LH012")
    private Timestamp LH012;
    @Column(name = "LH013")
    private BigDecimal LH013;
    @Column(name = "LH014")
    private BigDecimal LH014;
    @Column(name = "LH015")
    private BigDecimal LH015;
    @Column(name = "LH016")
    private BigDecimal LH016;
    @Column(name = "LH017")
    private BigDecimal LH017;
    @Column(name = "LH018")
    private String LH018;
    @Column(name = "LH019")
    private String LH019;
    @Column(name = "LH020")
    private String LH020;
    @Column(name = "LH021")
    private String LH021;
    @Column(name = "LH022")
    private String LH022;
    @Column(name = "LH023")
    private String LH023;
    @Column(name = "LH024")
    private String LH024;
    @Column(name = "LH025")
    private String LH025;
    @Column(name = "LH026")
    private String LH026;
    @Column(name = "LH027")
    private String LH027;
    @Column(name = "LH028")
    private String LH028;
    @Column(name = "LH029")
    private BigDecimal LH029;
    @Column(name = "LH030")
    private BigDecimal LH030;
    @Column(name = "LH031")
    private String LH031;
    @Column(name = "LH032")
    private String LH032;
    @Column(name = "LH033")
    private String LH033;
    @Column(name = "UDF01")
    private String UDF01;
    @Column(name = "UDF02")
    private String UDF02;
    @Column(name = "UDF03")
    private String UDF03;
    @Column(name = "UDF04")
    private String UDF04;
    @Column(name = "UDF05")
    private String UDF05;
    @Column(name = "UDF06")
    private BigDecimal UDF06;
    @Column(name = "UDF07")
    private BigDecimal UDF07;
    @Column(name = "UDF08")
    private BigDecimal UDF08;
    @Column(name = "UDF09")
    private BigDecimal UDF09;
    @Column(name = "UDF10")
    private BigDecimal UDF10;
}
