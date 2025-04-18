package org.faddy.community_feed.admin.ui.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTableListResponseDto<T> {
    
    private int totalCount;
    private List<T> tableData;         // 페이지에 표시될 데이터 목록
    
}