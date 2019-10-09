package com.fangdd.organization.server.business;

import com.fangdd.organization.server.invoker.UserInvoker;
import com.fangdd.organization.server.manager.OrgManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务逻辑类
 *
 * @author rocyuan
 * @since 2019-07-09 14:51
 */
@Component
public class OrgBusiness {

    @Autowired
    private UserInvoker userInvoker;

    @Autowired
    private OrgManager orgManager;

}
