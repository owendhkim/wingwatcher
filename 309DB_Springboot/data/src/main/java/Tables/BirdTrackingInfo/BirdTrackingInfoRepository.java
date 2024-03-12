package Tables.BirdTrackingInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BirdTrackingInfoRepository extends JpaRepository<BirdTrackingInfo, Long> {
    BirdTrackingInfo findById(int id);

    @Transactional
    void deleteById(int id);
}
