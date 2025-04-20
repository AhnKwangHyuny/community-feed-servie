package org.faddy.community_feed.admin.ui.query;

import org.faddy.community_feed.admin.ui.dto.GetTableListResponseDto;
import org.faddy.community_feed.admin.ui.dto.posts.GetPostTableRequestDto;
import org.faddy.community_feed.admin.ui.dto.posts.GetPostTableResponseDto;

public interface AdminPostTableQuery {
    GetTableListResponseDto<GetPostTableResponseDto> getPostTableData(GetPostTableRequestDto dto);
}
