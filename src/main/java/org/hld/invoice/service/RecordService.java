package org.hld.invoice.service;

import org.hld.invoice.entity.Record;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public interface RecordService {
    void createRecord(HttpServletRequest request, int userId, String name, String email, String content);

    List<Record> searchRecords(boolean fuzzy, Date start, java.util.Date end, String... params);
}
