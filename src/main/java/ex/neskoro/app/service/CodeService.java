package ex.neskoro.app.service;

import ex.neskoro.app.model.Code;
import ex.neskoro.app.rep.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class CodeService {
    private final CodeRepository codeRepository;

    @Autowired
    public CodeService(CodeRepository codeRepository) {
        this.codeRepository = codeRepository;
    }

    public Optional<Code> getCodeById(long id) {
        return codeRepository.findById(id);
    }

    public Code save(Code toSave) {
        return codeRepository.save(toSave);
    }

    public List<Code> getCode10Latest() {
        return codeRepository.findTop10ByTimeAndViewsOrderByDateDesc(0, 0);
    }

    public Optional<Code> getCodeByUUID(UUID uuid) {
        Code code = codeRepository.findByUuid(uuid);
        if (Objects.isNull(code)) {
            return Optional.empty();
        } else {
            return Optional.of(code);
        }
    }

    public Optional<Code> updateCodeViews(Code code) {
        if (code.getViews() == 0 && code.isViewsRestriction()) {
            codeRepository.deleteById(code.getId());
            return Optional.empty();
        } else if (code.getViews() == 0) {
            return Optional.of(code);
        } else {
            code.setViews(code.getViews() - 1);
            codeRepository.save(code);
            return Optional.of(code);
        }
    }

    public Optional<Code> updateCodeTime(Code code) {
        long timeBetween = ChronoUnit.SECONDS.between(LocalDateTime.now(), code.getDate().plusSeconds(code.getTime()));

        if (code.getTime() == 0) {
            return Optional.of(code);
        } else if (timeBetween > 0) {
            code.setTimeLeft(timeBetween);
            codeRepository.save(code);
            return Optional.of(code);
        } else {
            codeRepository.deleteById(code.getId());
            return Optional.empty();
        }
    }

}
