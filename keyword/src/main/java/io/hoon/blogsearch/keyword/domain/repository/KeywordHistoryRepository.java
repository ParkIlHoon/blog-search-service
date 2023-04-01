package io.hoon.blogsearch.keyword.domain.repository;

import io.hoon.blogsearch.keyword.domain.entity.KeywordHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface KeywordHistoryRepository extends JpaRepository<KeywordHistory, String> {

    boolean existsByName(String name);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE KeywordHistory k set k.count = k.count + 1 where k.name = :name")
    void increaseCount(@Param("name") String name);

    Page<KeywordHistory> findAll(Pageable pageable);

}
