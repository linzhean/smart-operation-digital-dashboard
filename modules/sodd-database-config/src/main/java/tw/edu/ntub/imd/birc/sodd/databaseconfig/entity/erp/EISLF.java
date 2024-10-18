package tw.edu.ntub.imd.birc.sodd.databaseconfig.entity.erp;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "EISLF")
@IdClass(EISLFId.class)
public class EISLF {
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
    @Column(name = "LF001")
    private String LF001;
    @Column(name = "LF002")
    private Timestamp LF002;
    @Column(name = "LF003")
    private BigDecimal LF003;
    @Column(name = "LF004")
    private BigDecimal LF004;
    @Column(name = "LF005")
    private BigDecimal LF005;
    @Column(name = "LF006")
    private BigDecimal LF006;
    @Column(name = "LF007")
    private String LF007;
    @Column(name = "LF008")
    private BigDecimal LF008;
    @Column(name = "LF009")
    private BigDecimal LF009;
    @Column(name = "LF010")
    private String LF010;
    @Column(name = "LF011")
    private String LF011;
    @Column(name = "LF012")
    private String LF012;
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
