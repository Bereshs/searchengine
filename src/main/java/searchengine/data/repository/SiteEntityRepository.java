package searchengine.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import searchengine.model.SiteEntity;

@Repository
public interface SiteEntityRepository extends JpaRepository<SiteEntity, Integer> {
    SiteEntity getByUrl(String url);


}
