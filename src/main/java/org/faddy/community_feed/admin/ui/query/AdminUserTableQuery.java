package org.faddy.community_feed.admin.ui.query;

import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;

public interface AdminUserTableQuery {
    GetTableListResponseDto<GetUserTableResponseDto> getUserTableData(GetUserTableRequestDto dto);
}
