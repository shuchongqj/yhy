package com.yhy.network.req;

import com.yhy.network.resp.Response;
import com.yhy.network.utils.ParameterList;
import com.yhy.network.utils.SecurityType;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
//_aid=1
// _ch=SJGW
// &containDistrictFlg=true
// &_mt=place.allActiveCitys
// &_session=92a2e658-e41f-4d79-8bb4-ce022e86ff2e
// &_ft=json
// &_did=368794230600
// &_vc=30160
// &_sig=ILBF3fDErzE616D%2FD3VxBktKNCk%3D
// &_domid=1000
public abstract class BaseRequest {

    /**
     * method name
     */
    private String _mt;

    /**
     * 渠道号
     */
    private String _ch;

    /**
     * deviceId有注册设备生成
     */
    private String _did;

    /**
     * session 注册设备返回的session
     */
    private String _session;

    /**
     * 请求格式写死json
     */
    private String _ft = "json";

    /**
     * 国际化，暂时没用
     */
    private String _lo;

    /**
     * appid目前写死1
     */
    private String _aid = "1";

    /**
     * userid
     */
    private String _uid;

    /**
     * 版本号
     */
    private String _vc;

    /**
     * domainId,目前写死1000
     */
    private String _domid = "1000";

    /**
     * 客户端IP
     */
    private String _cip;

    /**
     * 加密签名
     */
    private String _sig;

    /**
     * token
     */
    private String _tk;

    /**
     * device token
     */
    private String _dtk;

    /**
     * phone num
     */
    private String _pn;

    /**
     * 动态码
     */
    private String _dyn;


    /**
     * 安全类型
     */
    private int securityType = SecurityType.None;

    public String get_mt() {
        return _mt;
    }

    public void set_mt(String _mt) {
        this._mt = _mt;
    }

    public String get_ch() {
        return _ch;
    }

    public void set_ch(String _ch) {
        this._ch = _ch;
    }

    public String get_did() {
        return _did;
    }

    public void set_did(String _did) {
        this._did = _did;
    }

    public String get_session() {
        return _session;
    }

    public void set_session(String _session) {
        this._session = _session;
    }

    public String get_ft() {
        return _ft;
    }

    public void set_ft(String _ft) {
        this._ft = _ft;
    }

    public String get_lo() {
        return _lo;
    }

    public void set_lo(String _lo) {
        this._lo = _lo;
    }

    public String get_aid() {
        return _aid;
    }

    public void set_aid(String _aid) {
        this._aid = _aid;
    }

    public String get_uid() {
        return _uid;
    }

    public void set_uid(String _uid) {
        this._uid = _uid;
    }

    public String get_vc() {
        return _vc;
    }

    public void set_vc(String _vc) {
        this._vc = _vc;
    }

    public String get_domid() {
        return _domid;
    }

    public void set_domid(String _domid) {
        this._domid = _domid;
    }

    public String get_cip() {
        return _cip;
    }

    public void set_cip(String _cip) {
        this._cip = _cip;
    }

    public String get_sig() {
        return _sig;
    }

    public void set_sig(String _sig) {
        this._sig = _sig;
    }

    public int getSecurityType() {
        return securityType;
    }

    public void setSecurityType(int securityType) {
        this.securityType = securityType;
    }

    public String get_tk() {
        return _tk;
    }

    public void set_tk(String _tk) {
        this._tk = _tk;
    }

    public String get_dtk() {
        return _dtk;
    }

    public void set_dtk(String _dtk) {
        this._dtk = _dtk;
    }

    public String get_pn() {
        return _pn;
    }

    public void set_pn(String _pn) {
        this._pn = _pn;
    }

    public String get_dyn() {
        return _dyn;
    }

    public void set_dyn(String _dyn) {
        this._dyn = _dyn;
    }
}
