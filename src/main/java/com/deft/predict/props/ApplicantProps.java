package com.deft.predict.props;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */

@Setter
@Getter
@Component
//@RefreshScope // todo add it for future. We can dynamically update props.
@Configuration
@ConfigurationProperties("applicant")
public class ApplicantProps {

    private List<Double> priority;

}
