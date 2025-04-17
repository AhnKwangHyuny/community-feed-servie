package org.faddy.community_feed.admin.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.admin.ui.dto.GetDailyRegisteredUserResponseDto;
import org.faddy.community_feed.admin.ui.query.UserStatsQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserStatsQueryRepository userStatsQueryRepository;
    private final Integer BEFORE_DAYS = 10; //10일


    @GetMapping("/index")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");

        List<GetDailyRegisteredUserResponseDto> dailyRegisteredUserStats = userStatsQueryRepository.getDailyRegisteredUserStats(
            BEFORE_DAYS);

        System.out.println("dailyRegisteredUserStats.size() = " + dailyRegisteredUserStats.size());
        mav.addObject("result", userStatsQueryRepository.getDailyRegisteredUserStats(BEFORE_DAYS));
        return mav;
    }


}