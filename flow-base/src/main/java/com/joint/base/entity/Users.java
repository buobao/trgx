package com.joint.base.entity;


import com.joint.base.parent.BaseEntity;
import com.joint.base.util.excel.annotation.ExcelField;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name="sys_users")
public class Users extends BaseEntity {
	private static final long serialVersionUID = -1709852593225296320L;

    public Users(){
        super();
    }

    public Users(String name,Company company,String adminId){
        super();

        this.setName(name);
        this.setCompany(company);
        this.setAdminId(adminId);
    }

    /**
     * 帐号
     */
    private String adminId;
    /**
     * 编号
     */
    private String no;
	/**
	 * 姓名
	 */
	private String name;

	/**
	 * 拼音首字母
	 */
	private String pinYinHead;
	/**
	 * 拼音
	 */
	private String pinYin;
	/**
	 * 生日
	 */
	private Date birthday;
	/**
	 * 身份证号
	 */
	private String idCard;
    /**
     * 图像
     */
    private String fileId;

	/**
	 * 家庭住址
	 */
	private String address;
	/**
	 * 省
	 */
	private String province;
	/**
	 * 市
	 */
	private String city;
	/**
	 * 区
	 */
	private String county;
	/**
	 * 移动电话
	 */
	private String mobile;
	/**
	 * 固定电话
	 */
	private String phone;
	/**
	 * 终端电话
	 */
	private String terminal;
	/**
	 * email
	 */
	private String email;
	/**
	 * 用户积分
	 * */
	private long userPoint;
	/**
	 * 个人、签名
	 * */
	private String personalSign;
    /**
     * 密码
     */
    private String password;
    /**
     * 职责
     */
    private Set<Duty> dutySet;
    /**
     * 权限
     */
    private Set<Role> roleSet;
    /**
     * 用户关注的地图的显示坐标（经度/纬度）
     * */
    private BigDecimal longitude;
    private BigDecimal latitude;
	/**
	 * 账户种类（多选）
	 */
	private String adminType;

    /**
     * 是否是企业账户
     */
    private boolean isCompany;

    @ExcelField(title="用户名称", align=2, sort=20)
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinYinHead() {
		return pinYinHead;
	}
	public void setPinYinHead(String pinYinHead) {
		this.pinYinHead = pinYinHead;
	}
	public String getPinYin() {
		return pinYin;
	}
	public void setPinYin(String pinYin) {
		this.pinYin = pinYin;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
    @ExcelField(title="手机号", align=2, sort=20)
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}

    @ExcelField(title="邮箱", align=2, sort=20)
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getUserPoint() {
		return userPoint;
	}
	public void setUserPoint(long userPoint) {
		this.userPoint = userPoint;
	}
	public String getPersonalSign() {
		return personalSign;
	}
	public void setPersonalSign(String personalSign) {
		this.personalSign = personalSign;
	}

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    @OrderBy("id asc")
    public Set<Duty> getDutySet() {
        return dutySet;
    }


    public void setDutySet(Set<Duty> dutySet) {
        this.dutySet = dutySet;
    }
    @ManyToMany(cascade = {CascadeType.ALL},fetch = FetchType.LAZY, mappedBy = "usersSet")
    @OrderBy("name asc")
    public Set<Role> getRoleSet() {
        return roleSet;
    }

    public void setRoleSet(Set<Role> roleSet) {
        this.roleSet = roleSet;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    @ExcelField(title="编号", align=2, sort=20)
    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    @Column(precision=16,scale=8)
    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    @Column(precision=16,scale=8)
    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

	public String getAdminType() {
		return adminType;
	}

	public void setAdminType(String adminType) {
		this.adminType = adminType;
	}

    @Column(columnDefinition="tinyint(1) default 0")
    public boolean getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }

}
