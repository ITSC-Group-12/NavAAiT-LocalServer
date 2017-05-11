package com.itsc.team12.repository;

import com.itsc.team12.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Sam on 5/10/2017.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String>{

    User findByDeviceId(String deviceId);
    List<User> findByIsVisible(boolean isVisible);
}
