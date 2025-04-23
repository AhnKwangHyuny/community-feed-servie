package org.faddy.community_feed.post.service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Service for moderating content based on various rules.
 */
@Service
public class ContentModerationService {
    
    private static final int MAX_URL_COUNT = 3;
    private static final Pattern URL_PATTERN = Pattern.compile("(https?://[\\w-]+(\\.[\\w-]+)+([\\w.,@?^=%&:/~+#-]*[\\w@?^=%&/~+#-])?)");
    
    @Value("${content.moderation.enabled:true}")
    private boolean moderationEnabled;
    
    @Value("${content.moderation.banned-words:}")
    private String bannedWordsConfig;
    
    private List<String> bannedWords;
    
    public void init() {
        if (bannedWordsConfig != null && !bannedWordsConfig.isEmpty()) {
            bannedWords = Arrays.asList(bannedWordsConfig.toLowerCase().split(","));
        }
    }
    
    /**
     * Validates content against moderation rules
     * @param content Content to validate
     * @throws IllegalArgumentException if content violates moderation rules
     */
    public void validateContent(String content) {
        if (!moderationEnabled) {
            return;
        }
        
        // Empty content check is already handled by Post domain
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        
        // Check for banned words
        if (bannedWords != null && !bannedWords.isEmpty()) {
            String lowerContent = content.toLowerCase();
            for (String word : bannedWords) {
                if (lowerContent.contains(word)) {
                    throw new IllegalArgumentException("Content contains inappropriate language");
                }
            }
        }
        
        // Check URL limits
        java.util.regex.Matcher matcher = URL_PATTERN.matcher(content);
        int urlCount = 0;
        while (matcher.find()) {
            urlCount++;
            if (urlCount > MAX_URL_COUNT) {
                throw new IllegalArgumentException("Content contains too many URLs (max " + MAX_URL_COUNT + ")");
            }
        }
    }
} 