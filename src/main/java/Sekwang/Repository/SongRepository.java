package Sekwang.Repository;

import Sekwang.Domain.Song;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SongRepository extends JpaRepository<Song, Long> {
    Page<Song> findByCategoryOrderByCreatedAtDesc(Song.Category category, Pageable pageable);
}