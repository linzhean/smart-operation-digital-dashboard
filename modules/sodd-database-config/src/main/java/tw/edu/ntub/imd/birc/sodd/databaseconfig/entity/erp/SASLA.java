package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@IdClass(SASLAId.class)
public class SASLA {
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
    @Column(name = "LA001")
    private String la001;
    @Column(name = "LA002")
    private String la002;
    @Column(name = "LA003")
    private String la003;
    @Column(name = "LA004")
    private String la004;
    @Column(name = "LA005")
    private String la005;
    @Column(name = "LA006")
    private String la006;
    @Column(name = "LA007")
    private String la007;
    @Column(name = "LA008")
    private String la008;
    @Column(name = "LA009")
    private String la009;
    @Column(name = "LA010")
    private String la010;
    @Column(name = "LA011")
    private String la011;
    @Column(name = "LA012")
    private String la012;
    @Column(name = "LA013")
    private String la013;
    @Column(name = "LA014")
    private String la014;
    @Column(name = "LA015")
    private Timestamp la015;
    @Column(name = "LA016")
    private BigDecimal la016;
    @Column(name = "LA017")
    private BigDecimal la017;
    @Column(name = "LA018")
    private BigDecimal la018;
    @Column(name = "LA019")
    private BigDecimal la019;
    @Column(name = "LA020")
    private BigDecimal la020;
    @Column(name = "LA021")
    private BigDecimal la021;
    @Column(name = "LA022")
    private BigDecimal la022;
    @Column(name = "LA023")
    private BigDecimal la023;
    @Column(name = "LA024")
    private BigDecimal la024;
    @Column(name = "LA025")
    private BigDecimal la025;
    @Column(name = "LA026")
    private BigDecimal la026;
    @Column(name = "LA027")
    private BigDecimal la027;
    @Column(name = "LA028")
    private BigDecimal la028;
    @Column(name = "LA029")
    private String la029;
    @Column(name = "LA030")
    private String la030;
    @Column(name = "LA031")
    private String la031;
    @Column(name = "LA032")
    private String la032;
    @Column(name = "LA033")
    private String la033;
    @Column(name = "LA034")
    private String la034;
    @Column(name = "LA035")
    private String la035;
    @Column(name = "LA036")
    private String la036;
    @Column(name = "LA037")
    private String la037;
    @Column(name = "LA038")
    private String la038;
    @Column(name = "LA039")
    private String la039;
    @Column(name = "LA040")
    private String la040;
    @Column(name = "LA041")
    private String la041;
    @Column(name = "LA042")
    private String la042;
    @Column(name = "LA043")
    private String la043;
    @Column(name = "LA044")
    private String la044;
    @Column(name = "UDF01")
    private String udf01;
    @Column(name = "UDF02")
    private String udf02;
    @Column(name = "UDF03")
    private String udf03;
    @Column(name = "UDF04")
    private String udf04;
    @Column(name = "UDF05")
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
