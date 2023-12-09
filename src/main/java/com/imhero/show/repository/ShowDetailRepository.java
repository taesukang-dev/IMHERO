package com.imhero.show.repository;

import com.imhero.show.domain.ShowDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShowDetailRepository extends JpaRepository<ShowDetail, Long> {

    Optional<ShowDetail> findShowDetailByIdAndDelYn(Long id, String delYn);
}
