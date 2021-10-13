package com.example.dynamadbenhanced.controller;


import com.example.dynamadbenhanced.entity.Person;
import com.example.dynamadbenhanced.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PersonController {

    @Autowired
    PersonService personService;

    @GetMapping("/search/persons/{firstName}")
    public ResponseEntity<List<Person>> getPersonsByFirstName(@PathVariable(value = "firstName") String firstName) {
        return ResponseEntity.ok().body(personService.searchPersonsByFirstName(firstName));
    }

    @GetMapping("/search/persons/contains/{firstName}")
    public ResponseEntity<List<Person>> getPersonsContainsFirstName(@PathVariable(value = "firstName") String firstName) {
        return ResponseEntity.ok().body(personService.searchPersonsContainsFirstName(firstName));
    }

    @GetMapping("/get/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok().body(personService.getAllPersonUsingScan());
    }

    @GetMapping("/get/persons/personId/{id}")
    public ResponseEntity<List<Person>> getPersonById(@PathVariable(value = "id") String personId) {
        return ResponseEntity.ok().body(personService.getPersonsByPersonId(personId));
    }

    @GetMapping("/get/person/{id}/{age}")
    public ResponseEntity<Person> getSinglePerson(@PathVariable(value = "id") String personId, @PathVariable(value = "age") Integer age) {
        return ResponseEntity.ok().body(personService.getPersonByCompositePrimaryKey(personId, age));
    }

    @PostMapping("/create/person")
    public ResponseEntity<HttpStatus> createPerson(@RequestBody Person person) {
        personService.createPerson(person);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @PostMapping("/update/person")
    public ResponseEntity<String> updatePerson(@RequestBody Person person) {
        personService.updatePerson(person);
        return ResponseEntity.ok().body("ok");
    }

    @GetMapping("/delete/persons/{id}")
    public ResponseEntity<HttpStatus> deleteById(@PathVariable(value = "id") String personId) {
        personService.deletePersonsByPersonId(personId);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }

    @GetMapping("/delete/person/{id}/{age}")
    public ResponseEntity<HttpStatus> deleteByIdAndAge(@PathVariable(value = "id") String personId, @PathVariable(value = "age") Integer age) {
        personService.deletePersonByCompositePrimaryKey(personId, age);
        return ResponseEntity.ok().body(HttpStatus.OK);
    }
}
