package com.deft.predict.service;

import com.deft.predict.dto.ScoredApplicantListDTO;
import com.deft.predict.dto.TeamMemberApplicantDTO;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
public interface ApplicantService {

    ScoredApplicantListDTO evaluateCandidates(TeamMemberApplicantDTO teamMemberApplicantDTO);
}
