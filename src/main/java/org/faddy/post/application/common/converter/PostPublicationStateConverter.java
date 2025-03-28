package org.faddy.post.application.common.converter;


import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.faddy.post.domain.content.PostPublicationState;

@Converter(autoApply = true)
public class PostPublicationStateConverter implements AttributeConverter<PostPublicationState, String> {

    @Override
    public String convertToDatabaseColumn(PostPublicationState attribute) {
        return attribute != null ? attribute.getCode() : PostPublicationState.PUBLIC.getCode();
    }

    @Override
    public PostPublicationState convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return PostPublicationState.PUBLIC;
        }

        try {
            return PostPublicationState.getStateFromCode(dbData);
        } catch (IllegalArgumentException e) {
            // 로그 추가
            return PostPublicationState.PUBLIC;
        }
    }

}
