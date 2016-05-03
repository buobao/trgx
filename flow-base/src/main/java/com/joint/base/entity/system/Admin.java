package com.joint.base.entity.system;

import com.fz.us.base.bean.BaseEnum;
import com.joint.base.bean.EnumManage;
import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 实体类 - 管理员和登录用户
 * ============================================================================
 * 版权所有 2014 。
 * 
 * @author min_xu
 * 
 * @version 0.1 2014-01-20
 * ============================================================================
 */

@Entity
@Table(name="sys_admin")
public class Admin extends BaseEntity {

	private static final long serialVersionUID = -6665132594799061679L;

	//----------------------------------------------------------------------------
	public Admin() {
		super();
	}
	/**
     * 注册时使用（已经对密码进行加密处理）
	 * 简单admin对象
	 * @param username 账号
	 * @param password 加密过的密码
     * @param salt 盐值
	 * @param isAccountEnabled 是否启用
     *
	 */
	public Admin(String username, String password, String salt, Boolean isAccountEnabled,EnumManage.AccountType accountType) {
		super();
		this.username = username;
		this.password = password;
        this.salt = salt;
		this.isAccountEnabled = isAccountEnabled;
		this.isAccountExpired = false;
		this.isAccountLocked = false;
		this.isCredentialsExpired = false;
		this.payStatus = EnumManage.PayStatusEnum.free;
        this.accountType = accountType;
	}
    public Admin(String username, String password, String salt, Boolean isAccountEnabled, BaseEnum.AccountEnum accountEnum,EnumManage.AccountType accountType) {
        super();
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.isAccountEnabled = isAccountEnabled;
        this.accountEnum = accountEnum;
		this.isAccountExpired = false;
		this.isAccountLocked = false;
		this.isCredentialsExpired = false;
		this.payStatus = EnumManage.PayStatusEnum.free;
        this.accountType = accountType;
    }


	/**
	 * 返回对象属性Map集合
	 * username,password,isAccountEnabled(是/否),id
	 */
	public Map<String,Object> toMap() {
		Map<String,Object> row = new HashMap<String,Object>();
		row.put("username", username);
		row.put("password", password);
		row.put("isAccountEnabled", isAccountEnabled ? "是":"否");
		row.put("id", getId());
		return row;
	}

    /**
     * App deviceId
     */
    private String deviceId;
	/**
	 * 微信openId
	 */
	private String openId;
    /**
     * 微信企业号Id
     * */
    private String qyOpenId;
    /**
	 * 手机号(经过短信验证的手机号，可以经过登录)
	 */
	private String usermobile;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 密码
	 */
	private String password;
	/**
	 *  账号是否启用(账号激活时使用)
	 */
	private Boolean isAccountEnabled;
	/**
	 *  账号是否锁定(账号冻结停用时使用)
	 */
	private Boolean isAccountLocked;
    /**
     * 	账号锁定日期(操作停用时使用)
     */
    private Date lockedDate;

	/**
	 *  账号是否过期（有效期到期，使用该功能，通过到期日期配合）
	 */
	private Boolean isAccountExpired;
    /**
     * 账号到期日期
     */
    private Date expireDate;

	/**
	 *  凭证是否过期(登录超时需要重新登录)
	 */
	private Boolean isCredentialsExpired;

	/**
	 *  连续登录失败的次数(登录密码错误记录次数，持久化，存储到memcache)
	 */
	private Integer loginFailureCount;


	/**
	 *  最后登录日期（记录最后登陆日期，配合最后登陆的设备，通过LoginLog去实现）
	 */
	private Date loginDate;
	/**
	 *  最后登录IP
	 */
	private String loginIp;

    /**
     * 盐
     */
    private String salt;
    /**
     * 最后一次使用的用户名、手机号(在修改该记录时使用)
     * */
    private String lastUsername;
    private String lastUsermobile;

	/**
	 * 用户的账户类型，是付费用户还是免费用户
	 * */

	private EnumManage.PayStatusEnum payStatus;
    /**
     * 账户种类 企业/普通
     */
    private EnumManage.AccountType accountType;


    /**
     * 密码加密会根据账号的类型进行判断
     * */
    @Column()
    @Enumerated(EnumType.STRING)
    public BaseEnum.AccountEnum getAccountEnum() {
        return accountEnum;
    }

    public void setAccountEnum(BaseEnum.AccountEnum accountEnum) {
        this.accountEnum = accountEnum;
    }

    private BaseEnum.AccountEnum accountEnum;

    public String getLastUsername() {
        return lastUsername;
    }

    public void setLastUsername(String lastUsername) {
        this.lastUsername = lastUsername;
    }

    public String getLastUsermobile() {
        return lastUsermobile;
    }

    public void setLastUsermobile(String lastUsermobile) {
        this.lastUsermobile = lastUsermobile;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }
	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getQyOpenId() {
        return qyOpenId;
    }

    public void setQyOpenId(String qyOpenId) {
        this.qyOpenId = qyOpenId;
    }
	@Column()
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Column()
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column()
	public Boolean getIsAccountEnabled() {
		return isAccountEnabled;
	}

	public void setIsAccountEnabled(Boolean isAccountEnabled) {
		this.isAccountEnabled = isAccountEnabled;
	}

	@Column()
	public Boolean getIsAccountLocked() {
		return isAccountLocked;
	}

	public void setIsAccountLocked(Boolean isAccountLocked) {
		this.isAccountLocked = isAccountLocked;
	}

	@Column()
	public Boolean getIsAccountExpired() {
		return isAccountExpired;
	}

	public void setIsAccountExpired(Boolean isAccountExpired) {
		this.isAccountExpired = isAccountExpired;
	}

	@Column()
	public Boolean getIsCredentialsExpired() {
		return isCredentialsExpired;
	}

	public void setIsCredentialsExpired(Boolean isCredentialsExpired) {
		this.isCredentialsExpired = isCredentialsExpired;
	}

	@Column()
	public Integer getLoginFailureCount() {
		return loginFailureCount;
	}

	public void setLoginFailureCount(Integer loginFailureCount) {
		this.loginFailureCount = loginFailureCount;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public Date getLoginDate() {
		return loginDate;
	}

	public void setLoginDate(Date loginDate) {
		this.loginDate = loginDate;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	public EnumManage.PayStatusEnum getPayStatus() {
		return payStatus;
	}

	public void setPayStatus(EnumManage.PayStatusEnum payStatus) {
		this.payStatus = payStatus;
	}


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EnumManage.AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(EnumManage.AccountType accountType) {
        this.accountType = accountType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}