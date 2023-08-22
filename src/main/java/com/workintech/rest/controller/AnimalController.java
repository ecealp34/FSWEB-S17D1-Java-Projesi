package com.workintech.rest.controller;

import com.workintech.rest.entity.Animal;
import com.workintech.rest.mapping.AnimalResponse;
import com.workintech.rest.validation.AnimalValidation;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workintech/animal")
public class AnimalController {
    private Map<Integer, Animal> animalMap;

    @PostConstruct
    public void init() {
        animalMap = new HashMap<>();
    }

    @GetMapping("/")
    public List<Animal> get() {
        return animalMap.values().stream().toList();
    }

    @GetMapping("{id}")
    public AnimalResponse get(@PathVariable int id) {
        if (!AnimalValidation.isIdValid(id)) {
            return new AnimalResponse(null, "Id is not valid", 400);
        }
        if (!AnimalValidation.isMapContainsKey(animalMap, id)) {
            return new AnimalResponse(null, "Animal is not exist", 400);
        }
        return new AnimalResponse(animalMap.get(id), "Success", 200);
    }

    @PostMapping("/")
    public AnimalResponse save(@RequestBody Animal animal) {
        if (AnimalValidation.isMapContainsKey(animalMap, animal.getId())) {
            return new AnimalResponse(null, "Animal is already exist", 400);
        }
        if (!AnimalValidation.isAnimalCredentialIsValid(animal)) {
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }

        animalMap.put(animal.getId(), animal);
        return new AnimalResponse(animalMap.get(animal.getId()), "Animal is created successfully", 201);
    }

    @PutMapping("/{id}")
    public AnimalResponse update(@PathVariable int id, @RequestBody Animal animal) {
        if(!AnimalValidation.isMapContainsKey(animalMap, animal.getId())) {
            return new AnimalResponse(null, "Animal is not exist", 400);
        }
        if(!AnimalValidation.isAnimalCredentialIsValid(animal)) {
            return new AnimalResponse(null, "Animal credentials are not valid", 400);
        }
        animalMap.put(id, new Animal(id, animal.getName()));
        return new AnimalResponse(animalMap.get(id), "Animal is updated successfully", 200);
    }

    @DeleteMapping("/{id}")
    public AnimalResponse delete(@PathVariable int id) {
        if(!AnimalValidation.isMapContainsKey(animalMap, id)) {
            return new AnimalResponse(null, "Animal is not exist", 400);
        }
        animalMap.remove(id);
        return new AnimalResponse(animalMap.get(id), "Animal is removed successfully", 200);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("Animal Cotroller has been destroyed");
    }

}

