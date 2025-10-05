package Sekwang.Repository;

import Sekwang.Domain.FaithJournal;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaithJournalRepository extends JpaRepository<FaithJournal, Long> {
    Page<FaithJournal> findByAuthorUsernameOrderByCreatedAtDesc(String username, Pageable pageable);
}