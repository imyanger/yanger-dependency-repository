package com.yanger.test.service;

import com.yanger.test.dao.DataDao;
import com.yanger.test.mapper.DataMapper;
import com.yanger.test.module.T1;
import com.yanger.test.module.TestEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author yanger
 * @Date 2022/3/25/025 17:25
 */
@Service
public class DataService {

    @Autowired
    private DataDao dataDao;

    @Autowired
    private DataMapper dataMapper;

    public T1 save() {
        T1 t1 = new T1();
        dataDao.insert(t1);
        return t1;
    }


    public T1 saveMapper() {
        T1 t1 = new T1();
        t1.setName("mapper");
        t1.setEnumVal(TestEnum.B);
        dataMapper.insert(t1);
        return t1;
    }

    public void del(Integer id) {
        dataMapper.deleteById(id);
    }

}
