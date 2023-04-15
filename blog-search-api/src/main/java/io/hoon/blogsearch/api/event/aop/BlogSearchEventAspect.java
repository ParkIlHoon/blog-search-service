package io.hoon.blogsearch.api.event.aop;

import io.hoon.blogsearch.api.event.annotation.PublishBlogSearchEvent;
import io.hoon.blogsearch.api.event.publisher.BlogSearchEventPublisher;
import java.lang.reflect.Parameter;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * <h1>블로그 검색 이벤트 Aspect</h1>
 * {@link PublishBlogSearchEvent} 어노테이션이 붙은 메서드가 실행되기 전에 {@link io.hoon.blogsearch.api.event.BlogSearchEvent}를 발행합니다.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class BlogSearchEventAspect {

    private final BlogSearchEventPublisher publisher;

    @Before("@annotation(publishBlogSearchEvent)")
    public void publishBlogSearchEvent(JoinPoint joinPoint, PublishBlogSearchEvent publishBlogSearchEvent) {

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Parameter[] parameters = methodSignature.getMethod().getParameters();
        int keywordArgIndex = -1;

        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getName().equals(publishBlogSearchEvent.keywordArgName())) {
                keywordArgIndex = i;
                break;
            }
        }

        if (keywordArgIndex > -1) {
            String keyword = (String) joinPoint.getArgs()[keywordArgIndex];
            publisher.publishBlogSearchEvent(keyword);
        }
    }

}
