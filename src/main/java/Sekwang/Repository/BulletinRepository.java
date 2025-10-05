package Sekwang.Repository;

import Sekwang.Domain.Bulletin;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BulletinRepository extends JpaRepository<Bulletin, Integer> {
    Page<Bulletin> findAllByOrderByPublishDateDesc(org.springframework.data.domain.Pageable pageable);
}