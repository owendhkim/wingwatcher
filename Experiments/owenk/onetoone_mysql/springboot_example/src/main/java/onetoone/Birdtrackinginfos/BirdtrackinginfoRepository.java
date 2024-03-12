package onetoone.Birdtrackinginfos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Owen Kim
 *
 */

public interface BirdtrackinginfoRepository extends JpaRepository<Birdtrackinginfo, Long> {
    Birdtrackinginfo findById(int id);

    @Transactional
    void deleteById(int id);
}
