package com.example.estacApp.controller;


import com.example.estacApp.model.Carro;
import com.example.estacApp.repo.CarroRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
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
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(carroList, HttpStatus.OK);

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
        //return ResponseEntity.ok(carroObj);

    }

   @PutMapping("/updateCarroById/{id}")
    public ResponseEntity<Carro> updateCarroById(@PathVariable Long id, @RequestBody Carro newCarroData){
    Optional<Carro> oldCarroData =  carroRepo.findById(id);

    if(oldCarroData.isPresent()){
        // Pega a entidade existente do banco
        Carro updatedCarroData = oldCarroData.get();

        // Atualiza os dados com as informações recebidas
        updatedCarroData.setPlaca(newCarroData.getPlaca());
        updatedCarroData.setEntrada(newCarroData.getEntrada());
        updatedCarroData.setSaida(newCarroData.getSaida());

        // --- LÓGICA DE RECALCULAR O VALOR ---
        // Se ambos os horários estiverem presentes, recalcula o valor
        if (updatedCarroData.getEntrada() != null && updatedCarroData.getSaida() != null) {
            Duration duracao = Duration.between(updatedCarroData.getEntrada(), updatedCarroData.getSaida());
            long minutos = duracao.toMinutes();

            double valorPorHora = 5.0; // Mantenha a mesma lógica de preço
            double valortrue = (minutos / 60.0) * valorPorHora;
            updatedCarroData.setValor(valortrue);
        } else {
            // Se um dos horários for nulo, pode zerar o valor ou manter o antigo
            updatedCarroData.setValor(0.0);
        }

        // Salva a entidade completamente atualizada
        Carro carroSalvo = carroRepo.save(updatedCarroData);

        // Retorna o objeto atualizado com o novo valor
        return new ResponseEntity<>(carroSalvo, HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
}

    @DeleteMapping("/deleteCarroById/{id}")
    public ResponseEntity<HttpStatus> deleteCarroById(@PathVariable Long id){
        carroRepo.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
// a n sei oq sei q la