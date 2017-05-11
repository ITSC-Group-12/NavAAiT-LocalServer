package com.itsc.team12.web.rest;

import com.itsc.team12.entity.Location;
import com.itsc.team12.entity.User;
import com.itsc.team12.repository.UserRepository;
import com.itsc.team12.web.rest.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * Created by Sam on 5/10/2017.
 */
@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    //  TODO Testing needed
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> register(@RequestBody User user) throws URISyntaxException {
        if (user.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "idexists", "A new User cannot already have an ID")).body(null);
        }
        if (validator(user) != null) {
            return validator(user);
        }
        User result = userRepository.save(user);

        return ResponseEntity.created(new URI("/api/user/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert("User", result.getId().toString()))
                .body(result);
    }

    //  TODO Testing needed
    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<User> auth(@RequestBody User user) {
        if (validator(user) != null) {
            return validator(user);
        }
        User authUser = userRepository.findByDeviceId(user.getDeviceId());

        return Optional.ofNullable(authUser)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //  TODO Testing needed
    @RequestMapping(value = "/updateName", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<User> updateName(@RequestBody User user) {
        if (validator(user) != null) {
            return validator(user);
        }
        User result = userRepository.findByDeviceId(user.getDeviceId());

        if (result != null) {
            result.setFirstName(user.getFirstName());
            result.setLastName(user.getLastName());
            userRepository.save(result);
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("User", result.getId().toString()))
                    .body(result);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> setLocation(String deviceId, Location location) {
        return null;
    }

    public ResponseEntity<?> search(String firstName, String lastName) {
        return null;
    }

    //  TODO Testing needed
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAll() {

        List<User> users = userRepository.findByIsVisible(true);
        return users;
    }

    //  TODO  Testing needed
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> get(@PathVariable String id) {
        User user = userRepository.findOne(id);
        return Optional.ofNullable(user)
                .map(result -> new ResponseEntity<>(
                        result,
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    //  TODO Testing needed
    @RequestMapping(value = "/toggleVisiblity", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> toggleVisiblity(@RequestBody User user) {
        if (validator(user) != null) {
            return validator(user);
        }
        User result = userRepository.findByDeviceId(user.getDeviceId());
        if (result != null) {
            if (result.isVisible()) {
                result.setVisible(false);
            } else {
                result.setVisible(true);
            }
            userRepository.save(result);
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("User", result.getId().toString()))
                    .body(result);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity validator(User user) {
        if (user.getDeviceId() == null || user.getDeviceId().isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "nodeviceid", "No Device ID provided")).body(null);
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "firstname", "No First Name provided")).body(null);
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("User", "lastname", "No Last Name provided")).body(null);
        }
        return null;
    }

}
