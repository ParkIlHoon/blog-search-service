package io.hoon.blogsearch.support.searchclient.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

/**
 * <h1>의존성 방향 검증 테스트</h1>
 */
@AnalyzeClasses(packages = "io.hoon.blogsearch.support.searchclient", importOptions = { DoNotIncludeTests.class })
public class PackageDependencyTest {

    @ArchTest
    public static final ArchRule interfaces와_mapper_패키지_외_패키지에서는_interfaces_패키지에_의존성을_가질_수_없습니다 =
        noClasses()
            .that().resideOutsideOfPackages("..interfaces..", "..mapper..")
            .should()
            .dependOnClassesThat().resideInAPackage("..interfaces..");

}
