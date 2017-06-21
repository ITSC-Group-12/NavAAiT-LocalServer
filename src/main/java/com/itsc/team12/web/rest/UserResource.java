package com.itsc.team12.web.rest;

import com.itsc.team12.entity.Location;
import com.itsc.team12.entity.User;
import com.itsc.team12.repository.LocationRepository;
import com.itsc.team12.repository.UserRepository;
import com.itsc.team12.web.rest.util.HeaderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Sam on 5/10/2017.
 */
@RestController
@RequestMapping("/api/user")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationRepository locationRepository;

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

    @RequestMapping(value = "/setLocation", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseEntity<?> setLocation(@RequestBody User user) {
        if (validator(user) != null) {
            return validator(user);
        }
        if (user.getLocation() == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("Location", "location", "No Location Object provided")).body(null);
        }
        User result = userRepository.findByDeviceId(user.getDeviceId());

        if (result != null) {
            Location location = locationRepository.save(user.getLocation());
            result.setLocation(location);
            userRepository.save(result);
            return ResponseEntity.ok()
                    .headers(HeaderUtil.createEntityUpdateAlert("User", result.getId().toString()))
                    .body(result);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Set<User>> search(@RequestParam(value = "searchKey", required = true) String searchTerm) {
        if (searchTerm.isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("SearchTerm", "nosearchterm", "A Search Term is needed.")).body(null);
        }
        List<User> firstNameResult = userRepository.findByFirstNameLike(searchTerm);
        List<User> lastNameResult = userRepository.findByLastNameLike(searchTerm);
        Set<User> result = new LinkedHashSet<User>();
        result.addAll(firstNameResult);
        result.addAll(lastNameResult);

        return new ResponseEntity<Set<User>>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAll() {

        List<User> users = userRepository.findByIsVisible(true);
        return users;
    }

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
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("nodeviceid", "No Device ID provided")).body(null);
        }
        if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("firstname", "No First Name provided")).body(null);
        }
        if (user.getLastName() == null || user.getLastName().isEmpty()) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createAlert("lastname", "No Last Name provided")).body(null);
        }
        return null;
    }

}
