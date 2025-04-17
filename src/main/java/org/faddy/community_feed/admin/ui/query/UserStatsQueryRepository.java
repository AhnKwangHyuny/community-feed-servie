package org.faddy.community_feed.admin.ui.query;

import java.util.List;
import org.faddy.community_feed.admin.ui.dto.GetDailyRegisteredUserResponseDto;

public interface UserStatsQueryRepository {
    List<GetDailyRegisteredUserResponseDto> getDailyRegisteredUserStats(int beforeDays);
}
