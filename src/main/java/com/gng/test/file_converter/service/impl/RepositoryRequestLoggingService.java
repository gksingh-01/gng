package com.gng.test.file_converter.service.impl;

import com.gng.test.file_converter.model.RequestLog;
import com.gng.test.file_converter.repository.RequestLoggingRepository;
import com.gng.test.file_converter.service.RequestLoggingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepositoryRequestLoggingService implements RequestLoggingService {

    private final RequestLoggingRepository requestLoggingRepository;

    public RepositoryRequestLoggingService(@Autowired RequestLoggingRepository requestLoggingRepository) {
        this.requestLoggingRepository = requestLoggingRepository;
    }

    @Override
    public void log(RequestLog requestLog) {
        if (requestLog != null && requestLog.getId() != null) {
            requestLoggingRepository.save(requestLog);
        }
    }
}
