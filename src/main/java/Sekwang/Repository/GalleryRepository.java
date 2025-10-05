package Sekwang.Repository;

import Sekwang.Domain.GalleryItem;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryRepository extends JpaRepository<GalleryItem, Long> {
    Page<GalleryItem> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);
}