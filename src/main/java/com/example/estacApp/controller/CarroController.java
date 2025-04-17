package com.example.estacApp.controller;


import com.example.estacApp.model.Carro;
import com.example.estacApp.repo.CarroRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class CarroController {

    @Autowired
    private CarroRepo carroRepo;

    @GetMapping("/getAllCarros")
    public ResponseEntity<List<Carro>> getAllCarros(){
        try{
            List<Carro> carroList = new ArrayList<>();
            carroRepo.findAll().forEach(carroList::add);

            if(carroList.isEmpty()){
                return new ResponseEntity<>(carroList, HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(HttpStatus.OK);

        }catch (Exception ex){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/getCarroById/{id}")
    public ResponseEntity<Carro> getCarroById(@PathVariable Long id){
        Optional<Carro> carroData =  carroRepo.findById(id);

        if(carroData.isPresent()){
            return new ResponseEntity<>(carroData.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addCarro")
    public ResponseEntity<Carro> addCarro(@RequestBody Carro carro){
        if (carro.getEntrada() != null && carro.getSaida() != null) {
            Duration duracao = Duration.between(carro.getEntrada(), carro.getSaida());
            long minutos = duracao.toMinutes();

            double valorPorHora = 5.0;
            double valortrue = (minutos / 60.0) * valorPorHora;
            carro.setValor(valortrue);
        }else {
            // Caso a entrada ou saída esteja nula, opcionalmente retornar erro
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Carro carroObj = carroRepo.save(carro);
        return new ResponseEntity<>(carroObj, HttpStatus.OK);
    }

   /*@PostMapping("/updateCarroById/{id}")
    public ResponseEntity<Carro> updateCarroById(@PathVariable Long id, @RequestBody Carro newCarroData){
        Optional<Carro> oldCarroData =  carroRepo.findById(id);

        if(oldCarroData.isPresent()){
            Carro updatedCarroData = oldCarroData.get();
            updatedCarroData.setPlaca(newCarroData.getPlaca());
            updatedCarroData.setValor(newCarroData.getValor());

            Carro carro = carroRepo.save(updatedCarroData);
            return new ResponseEntity<>(carroObj, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }*/

    @DeleteMapping("/deleteCarroById/{id}")
    public ResponseEntity<HttpStatus> deleteCarroById(@PathVariable Long id){
        carroRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
