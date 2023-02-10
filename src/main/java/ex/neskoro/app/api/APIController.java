package ex.neskoro.app.api;

import ex.neskoro.app.model.Code;
import ex.neskoro.app.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class APIController {

    final
    CodeService codeService;

    @Autowired
    public APIController(CodeService codeService) {
        this.codeService = codeService;
    }

    @PostMapping("api/code/new")
    public ResponseEntity<?> createCode(@RequestBody Code code) {
        Code newCode = new Code(code.getCode(), code.getTime(), code.getViews());
        codeService.save(newCode);
        return new ResponseEntity<>(Map.of("id", String.valueOf(newCode.getUuid())), HttpStatus.OK);
    }

    @GetMapping(value = "api/code/{uuid}", produces = "application/json")
    public ResponseEntity<?> getCodeByUUID(@PathVariable UUID uuid) {
        Optional<Code> code = codeService.getCodeByUUID(uuid);

        if (code.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Code presentCode = code.get();

        if (presentCode.getTime() <= 0 && presentCode.getViews() <= 0 && !presentCode.isViewsRestriction()) {
            return prepareOkResponse(presentCode);
        } else if (presentCode.getViews() > 0 && presentCode.getTime() > 0) {
            code = codeService.updateCodeViews(presentCode);

            if (code.isPresent()) {
                code = codeService.updateCodeTime(code.get());
                return code.isPresent() ? prepareOkResponse(code.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } else if (presentCode.getViews() >= 0 && presentCode.isViewsRestriction()) {
            code = codeService.updateCodeViews(presentCode);
            return code.isPresent() ? prepareOkResponse(code.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            code = codeService.updateCodeTime(presentCode);
            return code.isPresent() ? prepareOkResponse(code.get()) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> prepareOkResponse(Code code) {
        return new ResponseEntity<>(Map.of("code", code.getCode(),
                "views", code.getViews(),
                "time", code.getTimeLeft(),
                "date", code.getFormattedDate()),
                HttpStatus.OK);
    }

    @GetMapping(value = "api/code/latest", produces = "application/json")
    public ResponseEntity<List<Code>> getCodeLatest() {
        return new ResponseEntity<>(codeService.getCode10Latest(), HttpStatus.OK);
    }

}
