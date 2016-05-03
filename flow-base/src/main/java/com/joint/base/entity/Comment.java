package com.joint.base.entity;

import com.fz.us.base.bean.BaseEnum;
import com.joint.base.bean.EnumManage;
import com.joint.base.parent.BaseEntity;

import javax.persistence.*;
import java.util.Set;

/**
 * Created with us-parent -> com.fz.us.core.entity.biz.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:03
 * 说明：
 */
@Entity
@Table(name="ss_comment")
public class Comment extends BaseEntity {
    private static final long serialVersionUID = -370316274269471216L;

    public Comment(){
        super();
    }

    public Comment(String text, Users toUsers){
        super();
        this.setText(text);
        this.setToUsers(toUsers);

    }

    public Comment(Users creater, String targetId, String target, Comment parent, EnumManage.CommentModelEnum modelEnum,String text,String atUsers){
        super();
        this.setText(text);
        this.setCreater(creater);
        this.setTargetId(targetId);
        this.setTargetClass(target);
        this.setParent(parent);
        this.setModel(modelEnum);
        this.setAtUsers(atUsers);
        this.setState(BaseEnum.StateEnum.Enable);
        this.setIfSystem(0);
        this.setIfRead(0);
        this.setCommentState(EnumManage.CommentState.response);
        this.setType(EnumManage.CommentTypeEnum.none);

    }

    /**
     * 批注接收人，多个接收人则分开成多个批注
     **/
    private Users toUsers;

    private String no;
    private String text;
    private int ifAgree;

    /**
     * 主题的显示名称
     * */
    private String subject;
    private String clientName;
    private String uriName;

    public final static String FN_ID = "___id";
    public final static String FN_CLASSNAME = "___class";

    /**
     * 目标对象id
     */
    private String targetId;
    /**
     * 目标对象class
     */
    private String targetClass;
    /**
     * 目标临时对象id
     */
    private String tempId;

    /**
     * 是否由系统发送、或者Users为空，默认0，系统1，企业账号不弹出框
     * */
    private int ifSystem;

    /**
     * 接受者是否已读，1为已读
     * */
    private int ifRead;
    /**
     * 状态
     * */
    private EnumManage.CommentState commentState;
    /**
     * 批注重要类别()
     * */
    private EnumManage.CommentTypeEnum type;
    /**
     * 父节点
     */
    private Comment parent;
    /**
     * 子节点
     */
    private Set<Comment> children;




    /**
     * 接收对象
     * */
    private String atUsers;

    private int ifFeedbackChecked;
    private String checkText;

    @Transient
    public String getFacedText() {
        return facedText;
    }

    public void setFacedText(String facedText) {
        this.facedText = facedText;
    }

    private String facedText;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public EnumManage.CommentModelEnum getModel() {
        return model;
    }

    public void setModel(EnumManage.CommentModelEnum model) {
        this.model = model;
    }

    private FileManage file;
    private EnumManage.CommentModelEnum model;

    @ManyToOne(fetch = FetchType.LAZY)
    public Comment getParent() {
        return parent;
    }

    public void setParent(Comment parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    @OrderBy("createDate asc")
    public Set<Comment> getChildren() {
        return children;
    }

    public void setChildren(Set<Comment> children) {
        this.children = children;
    }

    @Column(length = 32)
    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }
    @Column(length = 65535)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getIfAgree() {
        return ifAgree;
    }

    public void setIfAgree(int ifAgree) {
        this.ifAgree = ifAgree;
    }

    public String getTargetClass() {
        return targetClass;
    }

    public void setTargetClass(String targetClass) {
        this.targetClass = targetClass;
    }

    public int getIfSystem() {
        return ifSystem;
    }

    public void setIfSystem(int ifSystem) {
        this.ifSystem = ifSystem;
    }

    public EnumManage.CommentState getCommentState() {
        return commentState;
    }

    public void setCommentState(EnumManage.CommentState commentState) {
        this.commentState = commentState;
    }

    public EnumManage.CommentTypeEnum getType() {
        return type;
    }

    public void setType(EnumManage.CommentTypeEnum type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Users getToUsers() {
        return toUsers;
    }

    public void setToUsers(Users toUsers) {
        this.toUsers = toUsers;
    }

    public int getIfRead() {
        return ifRead;
    }

    public void setIfRead(int ifRead) {
        this.ifRead = ifRead;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    @Transient
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Transient
    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }
    @Transient
    public String getUriName() {
        return uriName;
    }

    public void setUriName(String uriName) {
        this.uriName = uriName;
    }



    public int getIfFeedbackChecked() {
        return ifFeedbackChecked;
    }

    public void setIfFeedbackChecked(int ifFeedbackChecked) {
        this.ifFeedbackChecked = ifFeedbackChecked;
    }

    public String getCheckText() {
        return checkText;
    }

    public void setCheckText(String checkText) {
        this.checkText = checkText;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public FileManage getFile() {
        return file;
    }
    public void setFile(FileManage file) {
        this.file = file;
    }

    public String getAtUsers() {
        return atUsers;
    }

    public void setAtUsers(String atUsers) {
        this.atUsers = atUsers;
    }

    private String createDateText;
}

