package com.itsc.team12.repository;

import com.itsc.team12.entity.Location;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Sam on 5/29/2017.
 */
public interface LocationRepository extends MongoRepository<Location, String> {
}
