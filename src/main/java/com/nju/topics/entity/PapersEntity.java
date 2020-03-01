package com.nju.topics.entity;

public class PapersEntity {

    private String SNO;

    //    @Field(type = FieldType.Text)
//    文章标题（中文）
    private String LYPM;

    //    @Field(type = FieldType.Keyword)
//    文章标题（英文）
    private String BLPM;

    //    @Field(type = FieldType.Keyword)
//    文章类型（一般要1/2的）
    private String WZXL;

    //    @Field(type = FieldType.Keyword)
//    期刊NO
    private String QKNO;

    //    @Field(type = FieldType.Keyword)
    private String XKDM1;

    //    @Field(type = FieldType.Keyword)
    private String XKDM2;

    //    @Field(type = FieldType.Keyword)
    private String YM;

    //    @Field(type = FieldType.Integer)
    private int YWSL;

    //    @Field(type = FieldType.Keyword)
//    关键词
    private String BYC;

    //    @Field(type = FieldType.Keyword)
    private String DCBJ;

    //    @Field(type = FieldType.Keyword)
    private String XMLX;

    //    @Field(type = FieldType.Keyword)
    private String JJLB;

    //    @Field(type = FieldType.Keyword)
    private String LRYMC;

    //    @Field(type = FieldType.Keyword)
    private String SKDM;

    //    @Field(type = FieldType.Keyword)
    private String YJDM;

    //    @Field(type = FieldType.Keyword)
    private String XKFL1;

    //    @Field(type = FieldType.Integer)
    private int YCFLAG;

    //    @Field(type = FieldType.Keyword)
    private String XKFL2;

    //    @Field(type = FieldType.Keyword)
    private String XKDM;

    //    @Field(type = FieldType.Keyword)
    private String NIAN;

    //    @Field(type = FieldType.Keyword)
    private String JUAN;

    //    @Field(type = FieldType.Keyword)
    private String QI;

    //    @Field(type = FieldType.Integer)
    private int QKLB;


    public int getYWSL() {
        return YWSL;
    }

    public String getBLPM() {
        return BLPM;
    }

    public String getLYPM() {
        return LYPM;
    }

    public String getQKNO() {
        return QKNO;
    }

    public String getSNO() {
        return SNO;
    }

    public String getWZXL() {
        return WZXL;
    }

    public void setBLPM(String BLPM) {
        this.BLPM = BLPM;
    }

    public String getXKDM1() {
        return XKDM1;
    }

    public void setLYPM(String LYPM) {
        this.LYPM = LYPM;
    }

    public void setQKNO(String QKNO) {
        this.QKNO = QKNO;
    }

    public void setSNO(String SNO) {
        this.SNO = SNO;
    }

    public String getXKDM2() {
        return XKDM2;
    }

    public String getYM() {
        return YM;
    }

    public void setWZXL(String WZXL) {
        this.WZXL = WZXL;
    }

    public void setXKDM1(String XKDM1) {
        this.XKDM1 = XKDM1;
    }

    public void setXKDM2(String XKDM2) {
        this.XKDM2 = XKDM2;
    }

    public void setYM(String YM) {
        this.YM = YM;
    }

    public int getQKLB() {
        return QKLB;
    }

    public int getYCFLAG() {
        return YCFLAG;
    }

    public String getBYC() {
        return BYC;
    }

    public void setYWSL(int YWSL) {
        this.YWSL = YWSL;
    }

    public String getDCBJ() {
        return DCBJ;
    }

    public String getJJLB() {
        return JJLB;
    }

    public void setBYC(String BYC) {
        this.BYC = BYC;
    }

    public String getJUAN() {
        return JUAN;
    }

    public String getLRYMC() {
        return LRYMC;
    }

    public String getNIAN() {
        return NIAN;
    }

    public String getQI() {
        return QI;
    }

    public String getSKDM() {
        return SKDM;
    }

    public String getXKDM() {
        return XKDM;
    }

    public String getXKFL1() {
        return XKFL1;
    }

    public String getXKFL2() {
        return XKFL2;
    }

    public String getYJDM() {
        return YJDM;
    }

    public String getXMLX() {
        return XMLX;
    }

    public void setDCBJ(String DCBJ) {
        this.DCBJ = DCBJ;
    }

    public void setJJLB(String JJLB) {
        this.JJLB = JJLB;
    }

    public void setJUAN(String JUAN) {
        this.JUAN = JUAN;
    }

    public void setLRYMC(String LRYMC) {
        this.LRYMC = LRYMC;
    }

    public void setNIAN(String NIAN) {
        this.NIAN = NIAN;
    }

    public void setQI(String QI) {
        this.QI = QI;
    }

    public void setQKLB(int QKLB) {
        this.QKLB = QKLB;
    }

    public void setSKDM(String SKDM) {
        this.SKDM = SKDM;
    }

    public void setXKDM(String XKDM) {
        this.XKDM = XKDM;
    }

    public void setXKFL1(String XKFL1) {
        this.XKFL1 = XKFL1;
    }

    public void setXKFL2(String XKFL2) {
        this.XKFL2 = XKFL2;
    }

    public void setXMLX(String XMLX) {
        this.XMLX = XMLX;
    }

    public void setYCFLAG(int YCFLAG) {
        this.YCFLAG = YCFLAG;
    }

    public void setYJDM(String YJDM) {
        this.YJDM = YJDM;
    }
}
