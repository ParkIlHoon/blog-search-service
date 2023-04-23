package io.hoon.blogsearch.domain.keyword.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "io.hoon.blogsearch.domain.keyword", importOptions = { DoNotIncludeTests.class })
public class PackageDependencyTest {

    @ArchTest
    public static final ArchRule interfaces_패키지는_domain_패키지에_의존성을_가질_수_없습니다 =
        noClasses()
            .that().resideInAPackage("io.hoon.blogsearch.domain.keyword.interfaces..")
            .should()
            .dependOnClassesThat().resideInAPackage("io.hoon.blogsearch.domain.keyword.domain..");

}
