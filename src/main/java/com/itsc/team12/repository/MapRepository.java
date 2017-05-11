package com.itsc.team12.repository;

import com.itsc.team12.entity.Map;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sam on 5/10/2017.
 */
@Repository
public interface MapRepository extends MongoRepository<Map, String>{

    Map findTop1ByOrderByCreatedDesc();
}
