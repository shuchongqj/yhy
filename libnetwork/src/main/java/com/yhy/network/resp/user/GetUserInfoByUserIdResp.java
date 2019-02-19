package com.yhy.network.resp.user;


public class GetUserInfoByUserIdResp {
    private long	id;	// id
    private long	options;	// 用户角色 按位与 1:会员 2:大咖 4：部长
    private String	avatar;	// 用户头像
    private String	nickname;	// 昵称
    private String	name;	// 用户姓名
    private String	gender;	// 性别 1-未确认 2-男 3-女
    private long	birthday;	// 生日
    private int	provinceCode;	// 常住地 省code
    private int	cityCode;	// 常住地 市code
    private String	signature;	// 个性签名
    private int	age;	// 年龄
    private String	liveStation;	// 原驻地
    private String	province;	// 省名称
    private String	city;	// 市名称
    private boolean	vip;	// 是否是会员
    private String	frontCover;	// 封面
    private boolean	isHasMainPage;	// 是否有主页
    private int	sportHobby;	// 运动爱好：0随便看看1篮球2羽毛球3足球4网球

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOptions() {
        return options;
    }

    public void setOptions(long options) {
        this.options = options;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public long getBirthday() {
        return birthday;
    }

    public void setBirthday(long birthday) {
        this.birthday = birthday;
    }

    public int getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(int provinceCode) {
        this.provinceCode = provinceCode;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getLiveStation() {
        return liveStation;
    }

    public void setLiveStation(String liveStation) {
        this.liveStation = liveStation;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isVip() {
        return vip;
    }

    public void setVip(boolean vip) {
        this.vip = vip;
    }

    public String getFrontCover() {
        return frontCover;
    }

    public void setFrontCover(String frontCover) {
        this.frontCover = frontCover;
    }

    public boolean isHasMainPage() {
        return isHasMainPage;
    }

    public void setHasMainPage(boolean hasMainPage) {
        isHasMainPage = hasMainPage;
    }

    public int getSportHobby() {
        return sportHobby;
    }

    public void setSportHobby(int sportHobby) {
        this.sportHobby = sportHobby;
    }
}
