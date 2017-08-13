package org.hld.invoice.service.impl;

import lombok.extern.log4j.Log4j;
import org.hld.invoice.common.utils.IPAddressUtil;
import org.hld.invoice.dao.RecordDao;
import org.hld.invoice.entity.Record;
import org.hld.invoice.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
@Log4j
@Transactional
public class RecordServiceImpl implements RecordService {

    private RecordDao recordDao;

    @Override
    public void createRecord(HttpServletRequest request, int userId, String name, String email, String content) {
        Record record = new Record();
        record.setTime(new Date());
        record.setIp(IPAddressUtil.getIpAddress(request));
        try {
            String address = IPAddressUtil.getAddresses(record.getIp(), "utf-8");
            record.setAddress(address == null ? "未知地理位置" : address);
        } catch (UnsupportedEncodingException e) {
            record.setAddress("未知地理位置");
        }
        record.setUserId(userId);
        record.setName(name);
        record.setEmail(email);
        record.setContent(content);
        recordDao.save(record);
    }

    @Override
    public List<Record> searchRecords(boolean fuzzy, Date start, Date end, String... params) {
        return recordDao.findRecords(fuzzy, start, end, params);
    }

    @Autowired
    public void setRecordDao(RecordDao recordDao) {
        this.recordDao = recordDao;
    }
}
