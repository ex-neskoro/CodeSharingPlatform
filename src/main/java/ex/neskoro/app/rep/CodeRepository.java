package ex.neskoro.app.rep;

import ex.neskoro.app.model.Code;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CodeRepository extends CrudRepository<Code, Long> {

    Code findByUuid(UUID uuid);
    
    List<Code> findTop10ByTimeAndViewsOrderByDateDesc(long time, int views);
}
