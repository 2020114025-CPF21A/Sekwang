package Sekwang.Repository;

import Sekwang.Domain.QtEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QtEntryRepository extends JpaRepository<QtEntry, Long> {
    java.util.List<QtEntry> findByMemberUsernameOrderByQtDateDesc(String username);
}

