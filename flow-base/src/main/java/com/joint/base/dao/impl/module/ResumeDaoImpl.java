package com.joint.base.dao.impl.module;

import com.fz.us.base.dao.impl.MongoBaseDaoImpl;
import com.joint.base.dao.module.ResumeDao;
import com.joint.base.entity.module.Resume;
import org.springframework.stereotype.Repository;

/**
 * Created with us-parent -> com.fz.us.modules.dao.impl.
 * User: min_xu
 * Date: 2014/9/28
 * Time: 16:46
 * 说明：
 */
@Repository
public class ResumeDaoImpl extends MongoBaseDaoImpl<Resume, String> implements ResumeDao {

}
