package com.example.demo.controller;

import com.example.demo.dto.PromoDTO;
import com.example.demo.exception.BadRequestException;
import com.example.demo.exception.NotFoundException;
import com.example.demo.model.Participant;
import com.example.demo.model.Prize;
import com.example.demo.model.Promo;
import com.example.demo.service.PromoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestController
@RequestMapping(value = "promo", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin
@Validated
public class PromoController {
    private final PromoService promoService;

    public PromoController(PromoService promoService){
        this.promoService = promoService;
    }

    @PostMapping
    Long createPromo(@RequestBody Promo promo){
        try {
            return promoService.createPromo(promo);
        }catch (ConstraintViolationException e){
            throw new BadRequestException("Error in request");
        }
    }

    @GetMapping
    List<PromoDTO> getPromos(){
        return promoService.getPromos();
    }

    @GetMapping("{id}")
    Promo getPromo(@PathVariable Long id){
        return promoService.getPromo(id);
    }

    @PutMapping("{id}")
    ResponseEntity<?> updatePromo(@PathVariable Long id, @RequestBody Promo promo){
        try {
            promoService.updatePromo(id, promo);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ConstraintViolationException e){
            throw new BadRequestException("Error in request");
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<?> deletePromo(@PathVariable Long id){
        promoService.deletePromo(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/participant")
    Long addParticipant(@PathVariable Long id, @RequestBody Participant participant){
        try {
            return promoService.addParticipant(id, participant);
        }catch (ConstraintViolationException e){
            throw new BadRequestException("Error in request");
        }

    }

    @DeleteMapping("{promoId}/participant/{participantId}")
    ResponseEntity<?> removeParticipant(@PathVariable Long promoId, @PathVariable Long participantId){
        promoService.removeParticipant(promoId, participantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/prize")
    Long addPrize(@PathVariable Long id, @RequestBody Prize prize){
        try {
            return promoService.addPrize(id, prize);
        }catch (ConstraintViolationException e){
            throw new BadRequestException("Error in request");
        }

    }

    @DeleteMapping("{promoId}/prize/{prizeId}")
    ResponseEntity<?> removePrize(@PathVariable Long promoId, @PathVariable Long prizeId){
        promoService.removePrize(promoId, prizeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("{id}/raffle")
    ResponseEntity<?> raffle(@PathVariable Long id){
        return promoService.raffle(id);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)

    public ResponseEntity<String> handleNotFoundException(NotFoundException e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
