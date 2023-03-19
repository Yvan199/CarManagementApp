package com.navy.cardatabase.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.navy.cardatabase.domain.Car;
import com.navy.cardatabase.domain.CarRepository;

@RestController
public class CarController {

	@Autowired
	private CarRepository repository;
	
	@GetMapping("/cars")
	public Iterable<Car> getCars(){
		
		return repository.findAll();
		
	}
}
