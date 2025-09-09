package com.gng.test.file_converter.repository;

import com.gng.test.file_converter.model.RequestLog;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestLoggingRepository extends CrudRepository<RequestLog, UUID> {

}
