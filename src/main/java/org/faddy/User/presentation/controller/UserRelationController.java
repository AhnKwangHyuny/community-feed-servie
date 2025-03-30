package org.faddy.User.presentation.controller;


import lombok.RequiredArgsConstructor;
import org.faddy.User.application.interfaces.UserRelationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/relations")
@RequiredArgsConstructor
public class UserRelationController {
    private final UserRelationService userRelationService;



}
