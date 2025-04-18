package org.faddy.community_feed.admin.ui.query;

import java.util.List;
import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.users.GetUserTableResponseDto;

public interface AdminTableQuery {
    GetTableListResponseDto<GetUserTableResponseDto> getUserTableData(GetUserTableRequestDto dto);
}
