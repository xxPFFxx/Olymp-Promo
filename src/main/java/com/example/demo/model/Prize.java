package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "prize")
public class Prize {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String description;

    @JsonIgnore
    @ManyToOne(optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "promo_id")
    private Promo promo;

    @JsonIgnore
    @OneToMany(mappedBy = "prize", fetch = FetchType.EAGER)
    private List<Results> resultsList;
}
