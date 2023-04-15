package io.hoon.blogsearch.api.event.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <h1>블로그 검색 이벤트 발행 어노테이션</h1>
 * @see io.hoon.blogsearch.api.event.aop.BlogSearchEventAspect
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PublishBlogSearchEvent {

    /**
     * 검색어 파라미터 명
     */
    String keywordArgName() default "keyword";

}
