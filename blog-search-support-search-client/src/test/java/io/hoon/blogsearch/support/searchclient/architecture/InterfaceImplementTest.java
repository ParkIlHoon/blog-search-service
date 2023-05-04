package io.hoon.blogsearch.support.searchclient.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import io.hoon.blogsearch.support.searchclient.interfaces.client.BlogSearchClient;

/**
 * <h1>인터페이스 구현 여부 검증 테스트</h1>
 */
@AnalyzeClasses(packages = "io.hoon.blogsearch.support.searchclient", importOptions = { DoNotIncludeTests.class })
public class InterfaceImplementTest {
    @ArchTest
    public static final ArchRule interfaces_client_패키지_내_클래스들은_BlogSearchClient_인터페이스를_구현해야합니다 =
        classes()
            .that()
            .resideInAPackage("..interfaces.client..")
            .and().areNotInterfaces()
            .should()
            .implement(BlogSearchClient.class);
}
