package com.example;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * Created by dmodh on 4/30/17.
 */
public interface RpiDataRepository extends MongoRepository<RpiData, Long> {

    public RpiData findByDateTime(String dateTime);

    public List<RpiData> findByTimestampBetween(long startTime, long endTime, Sort sort);

}
