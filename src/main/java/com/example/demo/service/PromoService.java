package com.example.demo.service;

import com.example.demo.dto.PromoDTO;
import com.example.demo.exception.NotFoundException;
import com.example.demo.mapper.PromoMapper;
import com.example.demo.model.Participant;
import com.example.demo.model.Prize;
import com.example.demo.model.Promo;
import com.example.demo.model.Results;
import com.example.demo.repository.ParticipantRepository;
import com.example.demo.repository.PrizeRepository;
import com.example.demo.repository.PromoRepository;
import com.example.demo.repository.ResultsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PromoService {
    private final PromoRepository promoRepository;
    private final PromoMapper promoMapper;
    private final ParticipantRepository participantRepository;
    private final PrizeRepository prizeRepository;
    private final ResultsRepository resultsRepository;

    public PromoService(PromoRepository promoRepository, PromoMapper promoMapper, ParticipantRepository participantRepository, PrizeRepository prizeRepository, ResultsRepository resultsRepository){
        this.promoRepository = promoRepository;
        this.promoMapper = promoMapper;
        this.participantRepository = participantRepository;
        this.prizeRepository = prizeRepository;
        this.resultsRepository = resultsRepository;
    }

    public Long createPromo(Promo promo) {
        Promo savedPromo = promoRepository.save(promo);
        return savedPromo.getId();
    }

    public List<PromoDTO> getPromos() {
        List<Promo> promoList = promoRepository.findAll();
        List<PromoDTO> promoDTOList = new ArrayList<>();
        for(Promo promo: promoList){
            promoDTOList.add(promoMapper.mapPromoToPromoDTO(promo));
        }
        return promoDTOList;
    }

    public Promo getPromo(Long id) {
        return promoRepository.findById(id).orElseThrow(() -> new NotFoundException("No promo with id " + id));
    }

    public void updatePromo(Long id, Promo promoToUpdate) {
        promoRepository.findById(id)
                .map(promo -> {
                    promo.setName(promoToUpdate.getName());
                    promo.setDescription(promoToUpdate.getDescription());
                    return promoRepository.save(promo);
                })
                .orElseThrow(() -> new NotFoundException("No promo with id " + id));
    }

    public void deletePromo(Long id) {
        promoRepository.findById(id).orElseThrow(() -> new NotFoundException("No promo with id " + id));
        promoRepository.deleteById(id);
    }

    public Long addParticipant(Long id, Participant participant) {
        Participant participant1 = participantRepository.save(participant);
        Promo promo = promoRepository.findById(id).orElseThrow(() -> new NotFoundException("No promo with id " + id));
        promo.getParticipants().add(participant1);
        promoRepository.save(promo);
        return participant1.getId();
    }

    public void removeParticipant(Long promoId, Long participantId) {
        Promo promo = promoRepository.findById(promoId).orElseThrow(() -> new NotFoundException("No promo with id " + promoId));
        Participant participant = participantRepository.findById(participantId).orElseThrow(() -> new NotFoundException("No participant with id " + participantId));
        promo.getParticipants().remove(participant);
        promoRepository.save(promo);
    }

    public Long addPrize(Long id, Prize prize) {
        Promo promo = promoRepository.findById(id).orElseThrow(() -> new NotFoundException("No promo with id " + id));
        prize.setPromo(promo);
        Prize prize1 = prizeRepository.save(prize);
        promo.getPrizes().add(prize1);
        promoRepository.save(promo);
        return prize1.getId();
    }

    public void removePrize(Long promoId, Long prizeId) {
        Promo promo = promoRepository.findById(promoId).orElseThrow(() -> new NotFoundException("No promo with id " + promoId));
        Prize prize = prizeRepository.findById(prizeId).orElseThrow(() -> new NotFoundException("No prize with id " + prizeId));
        promo.getPrizes().remove(prize);
        promoRepository.save(promo);
        if (prize.getPromo().getId() == promoId){
            prize.setPromo(null);
        }
        prizeRepository.save(prize);
    }

    public ResponseEntity<?> raffle(Long id) {
        Promo promo = promoRepository.findById(id).orElseThrow(() -> new NotFoundException("No promo with id " + id));
        if (promo.getParticipants().size() != promo.getPrizes().size()){
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        List<Results> resultsList = new ArrayList<>();
//        List<Participant> shuffledParticipants = promo.getParticipants();
//        Collections.shuffle(shuffledParticipants);
//        List<Participant> shuffledParticipants = promo.getParticipants();
//        Collections.shuffle(shuffledParticipants);
        for (int i = 0; i < promo.getParticipants().size(); i++){
            Results results = new Results(promo.getParticipants().get(i), promo.getPrizes().get(i));
            resultsRepository.save(results);
            resultsList.add(results);
        }
        return new ResponseEntity<>(resultsList, HttpStatus.OK);
    }
}
