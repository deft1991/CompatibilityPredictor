package com.deft.predict.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
@Getter
@Setter
@Component
@NoArgsConstructor
@AllArgsConstructor
public class ApplicantDTO {

    private String name;

    private AttributesDTO attributes;

}
