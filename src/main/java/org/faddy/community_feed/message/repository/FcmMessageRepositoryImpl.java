package org.faddy.community_feed.message.repository;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.faddy.community_feed.message.application.interfaces.MessageRepository;
import org.faddy.community_feed.message.domain.FcmTokenEntity;
import org.faddy.community_feed.message.repository.jpaRepository.JpaFcmTokenRepository;
import org.faddy.community_feed.user.domain.User;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
@RequiredArgsConstructor
public class FcmMessageRepositoryImpl implements MessageRepository {

    private final JpaFcmTokenRepository jpaFcmTokenRepository;

    private final String LIke_MESSAGE_TEMPLATE = "%s님이 %s님의 게시글에 좋아요를 눌렀습니다.";
    private final String MESSAGE_KEY = "message";

    @Override
    public void sendLikeMessage(User sendUser, User targetUser) {
        log.debug("Sending like message to user {}", sendUser);
        Optional<FcmTokenEntity> tokenEntity = jpaFcmTokenRepository.findById(targetUser.getId());

        if (tokenEntity.isEmpty()) {
            return; // 메세지 전송 x
        }

        FcmTokenEntity token = tokenEntity.get();

        //message 전송
        Message message = Message.builder()
            .putData(MESSAGE_KEY,
                String.format(LIke_MESSAGE_TEMPLATE, sendUser.getName(), targetUser.getName()))
            .setToken(token.getToken())
            .build();

        FirebaseMessaging.getInstance().sendAsync(message);
    }
}
