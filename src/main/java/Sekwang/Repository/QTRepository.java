// src/main/java/Sekwang/Repository/QTRepository.java
package Sekwang.Repository;

import Sekwang.Domain.QT;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QTRepository extends JpaRepository<QT, Long> {
    List<QT> findByUser_UsernameOrderByQtDateDesc(String username);
}
