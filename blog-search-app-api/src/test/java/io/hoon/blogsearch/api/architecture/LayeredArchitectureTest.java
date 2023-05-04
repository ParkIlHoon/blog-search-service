package io.hoon.blogsearch.api.architecture;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("레이어드 아키텍쳐 검증")
class LayeredArchitectureTest {

    private final JavaClasses allClasses = new ClassFileImporter(List.of(new DoNotIncludeTests())).importPackages("io.hoon.blogsearch");

    @Test
    @DisplayName("레이어드 아키텍쳐 준수")
    void 레이어드_아키텍쳐_준수_여부() {
        layeredArchitecture().consideringAllDependencies()

            // blog-search-app-api
            .layer("Presentation").definedBy("io.hoon.blogsearch.api.controller..")
            .layer("Business").definedBy("io.hoon.blogsearch.api..")

            // blog-search-domain-keyword
            .layer("Domain").definedBy("io.hoon.blogsearch.domain..")

            // blog-search-support-distribute-locker
            // blog-search-support-search-client
            .layer("Infrastructure").definedBy("io.hoon.blogsearch.support..")

            .whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
            .whereLayer("Business").mayOnlyBeAccessedByLayers("Presentation")
            .whereLayer("Domain").mayOnlyBeAccessedByLayers("Business")
            .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Business", "Domain")

            .check(allClasses);
    }
}
