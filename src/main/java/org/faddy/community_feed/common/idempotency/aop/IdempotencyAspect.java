package org.faddy.community_feed.common.idempotency.aop;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.faddy.community_feed.common.idempotency.Idempotency;
import org.faddy.community_feed.common.idempotency.repository.IdempotencyRepository;
import org.faddy.community_feed.common.ui.Response;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class IdempotencyAspect {
    private final IdempotencyRepository idempotencyRepository;
    private final HttpServletRequest request;

    public IdempotencyAspect(IdempotencyRepository idempotencyRepository, HttpServletRequest request) {
        this.idempotencyRepository = idempotencyRepository;
        this.request = request;
    }

    @Around("@annotation(org.faddy.community_feed.common.idempotency.annotation.Idempotent)")
    public Object checkIdempotency(ProceedingJoinPoint joinPoint) throws Throwable {
        String idempotencyKey = request.getHeader("Idempotency-Key");
        if (idempotencyKey == null) {
            return joinPoint.proceed(); // 에러를 반환해도 된다.
        }

        Idempotency existingResponse = idempotencyRepository.getByKey(idempotencyKey);
        if (existingResponse != null) {
            return existingResponse.getResponse();
        }

        Object result = joinPoint.proceed();
        Idempotency idempotency = new Idempotency(idempotencyKey, (Response<?>) result);
        idempotencyRepository.save(idempotency);
        return result;
    }
}