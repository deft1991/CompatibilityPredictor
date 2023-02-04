package com.deft.predict.service.impl;


import com.deft.predict.dto.*;
import com.deft.predict.exception.InvalidScoreException;
import com.deft.predict.props.ApplicantProps;
import com.deft.predict.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
@Service
@RequiredArgsConstructor
public class ApplicantServiceImpl implements ApplicantService {

    public static final String INTELLIGENCE = "Intelligence";
    public static final String STRENGTH = "Strength";
    public static final String ENDURANCE = "Endurance";
    public static final String SPICY_FOOD_TOLERANCE = "SpicyFoodTolerance";
    private final ApplicantProps applicantProps;

    public ScoredApplicantListDTO evaluateCandidates(TeamMemberApplicantDTO teamMemberApplicantDTO) {
        ScoredApplicantListDTO scoredApplicantListDTO = new ScoredApplicantListDTO();
        List<ScoredApplicantDTO> list = new ArrayList<>();

        HashMap<String, Double> priorityMap = getDefaultPriorityParams();
        prepareTeamScore(teamMemberApplicantDTO, priorityMap);

        /*
        Sort map based on value
         */
        priorityMap = priorityMap
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));


        fillPriorityMap(priorityMap);
        fillApplicantList(teamMemberApplicantDTO, list, priorityMap);
        scoredApplicantListDTO.setScoredApplicants(list);
        return scoredApplicantListDTO;
    }

    /**
     * todo I prefer to use Enums instead of strings
     */
    private static HashMap<String, Double> getDefaultPriorityParams() {
        HashMap<String, Double> priorityMap = new HashMap<>();
        priorityMap.put(INTELLIGENCE, 0.0);
        priorityMap.put(ENDURANCE, 0.0);
        priorityMap.put(STRENGTH, 0.0);
        priorityMap.put(SPICY_FOOD_TOLERANCE, 0.0);
        return priorityMap;
    }

    /**
     * Iterate over teamMembers and prepare their common score
     */
    private void prepareTeamScore(TeamMemberApplicantDTO teamMemberApplicantDTO, HashMap<String, Double> priorityMap) {
        for (TeamMemberDTO teamMemberDTO : teamMemberApplicantDTO.getTeam()) {

            //Throws invalid scores
            Integer teamMemberIntelligence = teamMemberDTO.getAttributes().getIntelligence();
            Integer teamMemberEndurance = teamMemberDTO.getAttributes().getEndurance();
            Integer teamMemberStrength = teamMemberDTO.getAttributes().getStrength();
            Integer teamMemberSpicyFoodTolerance = teamMemberDTO.getAttributes().getSpicyFoodTolerance();

            // todo add validator for it
            checkForInvalidScores(INTELLIGENCE, teamMemberIntelligence);
            checkForInvalidScores(ENDURANCE, teamMemberEndurance);
            checkForInvalidScores(STRENGTH, teamMemberStrength);
            checkForInvalidScores(SPICY_FOOD_TOLERANCE, teamMemberSpicyFoodTolerance);

            priorityMap.put("Intelligence", priorityMap.get("Intelligence") + teamMemberIntelligence);
            priorityMap.put("Endurance", priorityMap.get("Endurance") + teamMemberEndurance);
            priorityMap.put("Strength", priorityMap.get("Strength") + teamMemberStrength);
            priorityMap.put("SpicyFoodTolerance", priorityMap.get("SpicyFoodTolerance") + teamMemberSpicyFoodTolerance);
        }
    }

    /**
     *  Sets Max attribute to first priority and so on
     */
    private void fillPriorityMap(HashMap<String, Double> priorityMap) {
        int priorityPointer = 0;
        for (Map.Entry<String, Double> attribute : priorityMap.entrySet()){
            priorityMap.put(attribute.getKey(), applicantProps.getPriority().get(priorityPointer++));
        }
    }

    /**
     * Fill applicant list based on team score
     */
    private void fillApplicantList(TeamMemberApplicantDTO teamMemberApplicantDTO, List<ScoredApplicantDTO> list, HashMap<String, Double> priorityMap) {
        for (ApplicantDTO applicantDTO : teamMemberApplicantDTO.getApplicants()) {
            ScoredApplicantDTO scoredApplicantDTO = new ScoredApplicantDTO();
            scoredApplicantDTO.setName(applicantDTO.getName());

            Integer applicantIntelligence = applicantDTO.getAttributes().getIntelligence();
            Integer applicantStrength = applicantDTO.getAttributes().getStrength();
            Integer applicantEndurance = applicantDTO.getAttributes().getEndurance();
            Integer applicantSpicyFoodTolerance = applicantDTO.getAttributes().getSpicyFoodTolerance();

            // todo add check invalid scores
            checkForInvalidScores(INTELLIGENCE, applicantIntelligence);
            checkForInvalidScores(ENDURANCE, applicantEndurance);
            checkForInvalidScores(STRENGTH, applicantStrength);
            checkForInvalidScores(SPICY_FOOD_TOLERANCE, applicantSpicyFoodTolerance);

            //Finds average of four digits
            double totalScore = getTotalScore(priorityMap,
                    applicantIntelligence,
                    applicantStrength,
                    applicantEndurance,
                    applicantSpicyFoodTolerance);
            // todo move 10 to param. Same value as in validator
            scoredApplicantDTO.setScore(totalScore / (priorityMap.entrySet().size() * 10));
            list.add(scoredApplicantDTO);
        }
    }

    private static double getTotalScore(HashMap<String, Double> priorityMap, Integer applicantIntelligence, Integer applicantStrength, Integer applicantEndurance, Integer applicantSpicyFoodTolerance) {
        return applicantIntelligence * priorityMap.get(INTELLIGENCE) +
                applicantEndurance * priorityMap.get(ENDURANCE) +
                applicantStrength * priorityMap.get(STRENGTH) +
                applicantSpicyFoodTolerance * priorityMap.get(SPICY_FOOD_TOLERANCE);
    }

    /**
     * todo add check scores.
     * For now we dont have a limit, but later we can add it
     * Set it to 100 for example
     * todo move valid values to params
     */
    public void checkForInvalidScores (String attributeType, Integer value) throws InvalidScoreException {
        //todo add check
        // throw new InvalidScoreException();
    }
}
