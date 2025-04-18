package org.faddy.community_feed.admin.ui.dto.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.faddy.community_feed.common.utils.TimeCalculator;

@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetUserTableResponseDto {

    @Getter
    public Long userId;

    @Getter
    public String name;

    @Getter
    private String email;

    @Getter
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime lastLoginAt;

    public String getCreatedAt() {
        return TimeCalculator.getFormattedDate(createdAt);
    }

    public String getUpdatedAt() {
        return TimeCalculator.getFormattedDate(updatedAt);
    }

    public String getLastLoginAt() {
        return TimeCalculator.getFormattedDate(lastLoginAt);

    }

}
