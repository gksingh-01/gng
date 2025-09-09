package com.gng.test.file_converter.service.impl;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.gng.test.file_converter.model.RequestLog;
import com.gng.test.file_converter.repository.RequestLoggingRepository;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RepositoryRequestLoggingServiceTest {

    @Mock
    private RequestLoggingRepository requestLoggingRepository;

    @InjectMocks
    private RepositoryRequestLoggingService repositoryRequestLoggingService;

    @Test
    void logShouldCallRespositoryToSaveRequestLog() {
        RequestLog requestLog = new RequestLog();
        requestLog.setId(UUID.randomUUID());

        repositoryRequestLoggingService.log(requestLog);

        verify(requestLoggingRepository, times(1)).save(requestLog);
    }

    @Test
    void logShouldNotCallRespositoryIfRequestLogIsNull() {
        RequestLog requestLog = null;

        repositoryRequestLoggingService.log(requestLog);

        verify(requestLoggingRepository, never()).save(requestLog);
    }

    @Test
    void logShouldNotCallRepositoryIfIdIsNull() {
        RequestLog requestLog = new RequestLog();

        repositoryRequestLoggingService.log(requestLog);

        verify(requestLoggingRepository, never()).save(requestLog);
    }
}
