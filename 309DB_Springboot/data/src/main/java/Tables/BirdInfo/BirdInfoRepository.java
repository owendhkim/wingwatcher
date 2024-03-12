package Tables.BirdInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BirdInfoRepository extends JpaRepository<BirdInfo, Long> {
    BirdInfo findById(int id);

    @Transactional
    void deleteById(int id);
}
