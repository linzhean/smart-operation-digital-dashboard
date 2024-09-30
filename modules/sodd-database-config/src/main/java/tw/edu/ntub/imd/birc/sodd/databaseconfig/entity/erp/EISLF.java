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
    private String lf001;
    @Column(name = "LF002")
    private Timestamp lf002;
    @Column(name = "LF003")
    private BigDecimal lf003;
    @Column(name = "LF004")
    private BigDecimal lf004;
    @Column(name = "LF005")
    private BigDecimal lf005;
    @Column(name = "LF006")
    private BigDecimal lf006;
    @Column(name = "LF007")
    private String lf007;
    @Column(name = "LF008")
    private BigDecimal lf008;
    @Column(name = "LF009")
    private BigDecimal lf009;
    @Column(name = "LF010")
    private String lf010;
    @Column(name = "LF011")
    private String lf011;
    @Column(name = "LF012")
    private String lf012;
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
