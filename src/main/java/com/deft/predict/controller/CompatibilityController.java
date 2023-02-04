package com.deft.predict.controller;


import com.deft.predict.dto.ScoredApplicantListDTO;
import com.deft.predict.dto.TeamMemberApplicantDTO;
import com.deft.predict.service.impl.ApplicantServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Sergey Golitsyn
 * created on 03.02.2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/compatibility/")
public class CompatibilityController {

    private final ApplicantServiceImpl applicantServiceImpl;

    // todo make it post if we are going to save smth.
//    @PostMapping("/candidate") //
    @GetMapping("/candidate")
    public ScoredApplicantListDTO evaluateCandidates(@RequestBody TeamMemberApplicantDTO teamMemberApplicantDTO) {
        return applicantServiceImpl.evaluateCandidates(teamMemberApplicantDTO);
    }

}
