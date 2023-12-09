package com.imhero.show.repository;

import com.imhero.show.domain.Show;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ShowRepository extends JpaRepository<Show, Long> {
    Optional<Show> findShowByIdAndDelYn(Long id, String delYn);

    Page<Show> findAllByDelYn(Pageable pageable, String delYn);

    @Query(value = "SELECT *" +
            " FROM shows" +
            " WHERE MATCH(title, artist, place)" +
            " AGAINST(?1 IN NATURAL LANGUAGE MODE)" +
            " AND show_from_date BETWEEN ?2 and ?3" +
            " OR show_to_date BETWEEN ?2 and ?3" +
            " ORDER BY ID DESC",
            nativeQuery = true)
    Page<Show> findAllByFullTextSearch(Pageable pageable, String keyword, String fromDate, String toDate);
}
