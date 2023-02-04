package com.deft.predict.compatibilityservice.integration;

import com.deft.predict.PredictionApplication;
import com.deft.predict.dto.ApplicantDTO;
import com.deft.predict.dto.AttributesDTO;
import com.deft.predict.dto.TeamMemberApplicantDTO;
import com.deft.predict.dto.TeamMemberDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = PredictionApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration
@PropertySource(ignoreResourceNotFound = true, value = "classpath:application.yml")
public class CompatibilityControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${applicant.priority}")
    private List<Double> priority;

    @Test
    public void getApplicantsPositiveTest() throws Exception {

        AttributesDTO teamMember1AttributesDTO = new AttributesDTO(1, 10, 1, 1);
        AttributesDTO teamMember2AttributesDTO = new AttributesDTO(10, 1, 1, 1);
        TeamMemberDTO teamMember1 = new TeamMemberDTO("1", teamMember1AttributesDTO);
        TeamMemberDTO teamMember2 = new TeamMemberDTO("2", teamMember2AttributesDTO);
        List<TeamMemberDTO> teamList = new ArrayList<>();
        teamList.add(teamMember1);
        teamList.add(teamMember2);

        AttributesDTO applicant1AttributesDTO = new AttributesDTO(10, 10, 1, 1);
        AttributesDTO applicant2AttributesDTO = new AttributesDTO(1, 1, 10, 10);
        ApplicantDTO applicant1DTO = new ApplicantDTO("1", applicant1AttributesDTO);
        ApplicantDTO applicant2DTO = new ApplicantDTO("2", applicant2AttributesDTO);
        List<ApplicantDTO> applicantList = new ArrayList<>();
        applicantList.add(applicant1DTO);
        applicantList.add(applicant2DTO);

        TeamMemberApplicantDTO expected = new TeamMemberApplicantDTO(teamList, applicantList);

        HashMap<String, Double> priorityMap = new LinkedHashMap<>();
        priorityMap.put("Intelligence", 0.0);
        priorityMap.put("Endurance", 0.0);
        priorityMap.put("Strength", 0.0);
        priorityMap.put("SpicyFoodTolerance", 0.0);

        priorityMap.put("Intelligence", priorityMap.get("Intelligence") +
                teamMember1.getAttributes().getIntelligence() + teamMember2.getAttributes().getIntelligence());
        priorityMap.put("Strength", priorityMap.get("Strength") +
                teamMember1.getAttributes().getStrength() + teamMember2.getAttributes().getStrength());
        priorityMap.put("Endurance", priorityMap.get("Endurance") +
                teamMember1.getAttributes().getEndurance() + teamMember2.getAttributes().getEndurance());
        priorityMap.put("SpicyFoodTolerance", priorityMap.get("SpicyFoodTolerance") +
                teamMember1.getAttributes().getSpicyFoodTolerance() + teamMember2.getAttributes().getSpicyFoodTolerance());

        priorityMap = priorityMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        int priorityPointer = 0;
        for (Map.Entry<String, Double> attribute : priorityMap.entrySet()){
            priorityMap.put(attribute.getKey(), priority.get(priorityPointer++));
        }

        //Finds average of four digits
        Double expectedScore1 = (applicant1DTO.getAttributes().getIntelligence() * priorityMap.get("Intelligence") +
                applicant1DTO.getAttributes().getStrength() * priorityMap.get("Strength") +
                applicant1DTO.getAttributes().getEndurance() * priorityMap.get("Endurance") +
                applicant1DTO.getAttributes().getSpicyFoodTolerance() * priorityMap.get("SpicyFoodTolerance")) / 40.0;

        Double expectedScore2 = (applicant2DTO.getAttributes().getIntelligence() * priorityMap.get("Intelligence") +
                applicant2DTO.getAttributes().getStrength() * priorityMap.get("Strength") +
                applicant2DTO.getAttributes().getEndurance() * priorityMap.get("Endurance") +
                applicant2DTO.getAttributes().getSpicyFoodTolerance() * priorityMap.get("SpicyFoodTolerance")) / 40.0;

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/compatibility/candidate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expected))
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoredApplicants.[0].name", Matchers.is("1")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoredApplicants.[1].name", Matchers.is("2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoredApplicants.[0].score", Matchers.is(expectedScore1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.scoredApplicants.[1].score", Matchers.is(expectedScore2)));


    }
}
