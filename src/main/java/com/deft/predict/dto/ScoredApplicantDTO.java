package com.deft.predict.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
@Getter
@Setter
@Component
public class ScoredApplicantDTO {

    private String name;

    private Double score;
}
