package org.faddy.community_feed.admin.ui.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDailyRegisteredUserResponseDto {
    private LocalDate date;
    private Long count;
}
