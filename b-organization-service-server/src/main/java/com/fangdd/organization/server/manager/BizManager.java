package com.fangdd.organization.server.manager;

import com.fangdd.organization.server.dao.BizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * @author lantian
 * @date 2019/10/09
 */
@Repository
public class BizManager {

    @Autowired
    private BizMapper bizMapper;

    public Date getTime(){
        return bizMapper.selectTime();
    }



}
