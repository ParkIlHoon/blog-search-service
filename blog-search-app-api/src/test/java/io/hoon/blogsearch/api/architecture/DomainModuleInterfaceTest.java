package io.hoon.blogsearch.api.architecture;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * <h1>도메인 모듈의 인터페이스 패키지 및 접근 가능 패키지 제한 테스트</h1>
 *
 * 도메인 모듈에 이 테스트가 위치할 경우, 해당 도메인 모듈이 의존하지 않는 모듈은 테스트 대상에서 제외되어 최상위 레이어에 위치시킴.
 */
@DisplayName("도메인 모듈 인터페이스 테스트")
public class DomainModuleInterfaceTest {

    private final JavaClasses allClasses = new ClassFileImporter(List.of(new DoNotIncludeTests())).importPackages("io.hoon.blogsearch");

    @Test
    @DisplayName("도메인 모듈은 인터페이스 패키지를 통해서만 접근 가능")
    void only_by_interfaces_package() {
        layeredArchitecture().consideringAllDependencies()

            .layer("All").definedBy("io.hoon.blogsearch..")
            .layer("Interface").definedBy("io.hoon.blogsearch.domain.*.interfaces..")
            .layer("Conseal").definedBy("io.hoon.blogsearch.domain.*.domain..", "io.hoon.blogsearch.domain.*.configuration..")

            .whereLayer("Conseal").mayNotBeAccessedByAnyLayer()
            .whereLayer("Interface").mayOnlyBeAccessedByLayers("All")

            .check(allClasses);
    }
}
