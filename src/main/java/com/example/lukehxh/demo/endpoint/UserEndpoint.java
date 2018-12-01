package com.example.lukehxh.demo.endpoint;

import com.example.lukehxh.demo.exceptions.ResourceNotFoundException;
import com.example.lukehxh.demo.model.User;
import com.example.lukehxh.demo.repository.UserRepository;
import com.example.lukehxh.demo.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserEndpoint {

    private final DateUtil dateUtil;
    private final UserRepository userDAO;

    @Autowired
    public UserEndpoint(DateUtil dateUtil, UserRepository userDAO) {
        this.dateUtil = dateUtil; // testing with date
        this.userDAO = userDAO;
    }

    @GetMapping
    public ResponseEntity<?> listAll() {
        return new ResponseEntity<>(this.userDAO.findAll(), HttpStatus.OK);
    }

    @GetMapping(path = "/find/{id}")
    public ResponseEntity<?> getPersonById(@PathVariable("id") Long id) {

        verifyIfUserExists(id);

        Optional<User> userOptional = this.userDAO.findById(id);
        @SuppressWarnings("OptionalGetWithoutIsPresent") User user = userOptional.get();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = "/search/{name}")
    public ResponseEntity<?> getPersonByName(@PathVariable("name") String name) {
        List<User> listUsers = this.userDAO.findByNameIgnoreCaseContaining(name);

        if (listUsers.isEmpty())
            throw new ResourceNotFoundException("No user like '" + name + "' was found!");

        return new ResponseEntity<>(listUsers, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody User user) {
        return new ResponseEntity<>(this.userDAO.save(user), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        this.userDAO.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody User user) {
        this.userDAO.save(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/today")
    public String dateAccess(){
        return dateUtil.formateLocalDateTimeToDatabaseStyle(LocalDateTime.now());
    }

    private void verifyIfUserExists(Long id) {
        Optional<User> userOptional = this.userDAO.findById(id);

        if (!userOptional.isPresent())
            throw new ResourceNotFoundException("User not found for ID " + id);
    }
}
