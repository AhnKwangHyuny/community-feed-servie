package org.faddy.community_feed.admin.ui;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetDailyRegisteredUserResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;
import org.faddy.community_feed.admin.ui.query.AdminTableQuery;
import org.faddy.community_feed.admin.ui.query.UserStatsQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserStatsQueryRepository userStatsQueryRepository;
    private final AdminTableQuery adminUserRepository;

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

    @GetMapping("/users")  // URL 경로가 users인 것 같습니다(템플릿에서 확인)
    public ModelAndView user(@ModelAttribute GetUserTableRequestDto requestDto) {

        System.out.println("requestDto.getOffset() = " + requestDto.getOffset());
        
        ModelAndView mav = new ModelAndView();  
        mav.setViewName("users");  // 뷰 이름도 users로 수정

        GetTableListResponseDto<GetUserTableResponseDto> userTableData = adminUserRepository.getUserTableData(requestDto);

        // 뷰에서 사용하는 변수명에 맞게 데이터 전달
        mav.addObject("userList", userTableData.getTableData());
        mav.addObject("totalCount", userTableData.getTotalCount());
        mav.addObject("requestDto", requestDto);

        return mav;
    }

}