package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "results")
public class Results {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "winner_id")
    private Participant winner;

    @ManyToOne(optional = true, cascade =  CascadeType.ALL)
    @JoinColumn(name = "prize_id")
    private Prize prize;

    public Results(Participant participant, Prize prize) {
        this.winner = participant;
        this.prize = prize;
    }
}
