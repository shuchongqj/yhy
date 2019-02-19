package com.newyhy.beans;

import java.io.Serializable;

/**
 * UserInfo
 * Created by Jiervs on 2018/6/26.
 */

public class UserInfo implements Serializable{

    private static final long serialVersionUID = 3L;

    public long	userId;	// 用户ID
    public String	avatar;	// 头像
    public String	gender;	// 性别FEMALE女 MALE男 INVALID_GENDER未知
    public String	nickname;	// 昵称
    public String	name;	// 名称
    public String	level;	// 级别，VERIFIED：加v的, NORMAL 普通的,DOCTOR 医生,AGENCY 机构，FAMOUS 名人
    public String	introduction;	// 描述
    public String	currentUserFollowStatus;	// 当前用户关注类型 UNFOLLOW(不关注), FOLLOW(关注), BOTHFOLLOW(互相关注)
    public long	followerNum;	// 粉丝数
    public String	userType;	// 用户类型
    public long	options;	// 用户角色
}
