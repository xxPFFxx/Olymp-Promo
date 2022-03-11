package com.example.demo.mapper;

import com.example.demo.dto.PromoDTO;
import com.example.demo.model.Promo;
import org.springframework.stereotype.Component;

@Component
public class PromoMapper {

    public PromoDTO mapPromoToPromoDTO(Promo promo){
        PromoDTO promoDTO = new PromoDTO();
        promoDTO.setId(promo.getId());
        promoDTO.setName(promo.getName());
        promoDTO.setDescription(promo.getDescription());
        return promoDTO;
    }
}
