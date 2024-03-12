package Tables.Analytics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface AnalyticRepository extends JpaRepository<Analytic, Long> {
    Analytic findById(int id);

    @Transactional
    void deleteById(int id);
}