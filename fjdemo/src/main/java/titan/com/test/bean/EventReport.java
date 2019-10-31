package titan.com.test.bean;

import java.io.Serializable;
import java.util.ArrayList;


public class EventReport implements Serializable {

    private static final long serialVersionUID = 7963626502281085646L;

    public Long getLID() {
        return LID;
    }

    public void setLID(Long LID) {
        this.LID = LID;
    }

    public String getXJ_ID() {
        return XJ_ID;
    }

    public void setXJ_ID(String XJ_ID) {
        this.XJ_ID = XJ_ID;
    }

    public String getXJ_SJMC() {
        return XJ_SJMC;
    }

    public void setXJ_SJMC(String XJ_SJMC) {
        this.XJ_SJMC = XJ_SJMC;
    }

    public String getXJ_SJLX() {
        return XJ_SJLX;
    }

    public void setXJ_SJLX(String XJ_SJLX) {
        this.XJ_SJLX = XJ_SJLX;
    }

    public String getXJ_SBBH() {
        return XJ_SBBH;
    }

    public void setXJ_SBBH(String XJ_SBBH) {
        this.XJ_SBBH = XJ_SBBH;
    }

    public ArrayList<UpFile> getXJ_ZPDZ() {
        return XJ_ZPDZ;
    }

    public void setXJ_ZPDZ(ArrayList<UpFile> XJ_ZPDZ) {
        this.XJ_ZPDZ = XJ_ZPDZ;
    }

    public ArrayList<UpFile> getXJ_SPDZ() {
        return XJ_SPDZ;
    }

    public void setXJ_SPDZ(ArrayList<UpFile> XJ_SPDZ) {
        this.XJ_SPDZ = XJ_SPDZ;
    }

    public ArrayList<UpFile> getXJ_YPDZ() {
        return XJ_YPDZ;
    }

    public void setXJ_YPDZ(ArrayList<UpFile> XJ_YPDZ) {
        this.XJ_YPDZ = XJ_YPDZ;
    }

    public String getXJ_MSXX() {
        return XJ_MSXX;
    }

    public void setXJ_MSXX(String XJ_MSXX) {
        this.XJ_MSXX = XJ_MSXX;
    }

    public String getXJ_SCRQ() {
        return XJ_SCRQ;
    }

    public void setXJ_SCRQ(String XJ_SCRQ) {
        this.XJ_SCRQ = XJ_SCRQ;
    }

    public String getXJ_JD() {
        return XJ_JD;
    }

    public void setXJ_JD(String XJ_JD) {
        this.XJ_JD = XJ_JD;
    }

    public String getXJ_WD() {
        return XJ_WD;
    }

    public void setXJ_WD(String XJ_WD) {
        this.XJ_WD = XJ_WD;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getXJ_XXDZ() {
        return XJ_XXDZ;
    }

    public void setXJ_XXDZ(String XJ_XXDZ) {
        this.XJ_XXDZ = XJ_XXDZ;
    }

    public String getXC_ID() {
        return XC_ID;
    }

    public void setXC_ID(String XC_ID) {
        this.XC_ID = XC_ID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    private Long LID;//本地保存id

    //[Display(Name = "主键ID")]
    private String XJ_ID;

    //[Display(Name = "XJ_SJMC 事件描述")]
    private String XJ_SJMC;

    //[Display(Name = "事件类型")]
    private String XJ_SJLX;

    //[Display(Name = "XJ_SBBH 设备编号")]
    private String XJ_SBBH;

    //[Display(Name = "XJ_ZPDZ 照片地址")]
    private ArrayList<UpFile> XJ_ZPDZ;

    //[Display(Name = "XJ_SPDZ 视频地址")]
    private ArrayList<UpFile> XJ_SPDZ;

    //[Display(Name = "XJ_YPDZ 音频地址")]
    private ArrayList<UpFile> XJ_YPDZ;

    //[Display(Name = "XJ_MSXX 描述信息")]
    private String XJ_MSXX;

    //[Display(Name = "XJ_SCRQ 上传日期")]
    private String XJ_SCRQ;

    //[Display(Name = "XJ_JD 经度")]
    private String XJ_JD;

    //[Display(Name = "XJ_WD 纬度")]
    private String XJ_WD;

    //[Display(Name = "REMARK 备注")]
    private String REMARK;

    //[Display(Name = "XJ_XXDZ 详细地址")]
    private String XJ_XXDZ;

    //[Display(Name = "XC_ID")]
    private String XC_ID;

    //[Display(Name = "USERID")]
    private String USERID;

    public ArrayList<XJLX> getXJ_LX() {
        return XJ_LX;
    }

    public void setXJ_LX(ArrayList<XJLX> XJ_LX) {
        this.XJ_LX = XJ_LX;
    }

    private ArrayList<XJLX> XJ_LX;


    public static class XJLX{
        public String getSJ_DL() {
            return SJ_DL;
        }

        public void setSJ_DL(String SJ_DL) {
            this.SJ_DL = SJ_DL;
        }

        public String getSJ_XL() {
            return SJ_XL;
        }

        public void setSJ_XL(String SJ_XL) {
            this.SJ_XL = SJ_XL;
        }

        private String SJ_DL;
        private String SJ_XL;
    }




}
