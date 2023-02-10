package ex.neskoro.app.web;

import ex.neskoro.app.model.Code;
import ex.neskoro.app.LocalPaths;
import ex.neskoro.app.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

@Controller
public class WebController {

    final CodeService codeService;

    @Autowired
    public WebController(CodeService codeService) {
        this.codeService = codeService;
    }

    @GetMapping(value = "/code/new", produces = "text/html")
    public ResponseEntity<String> getNewCodeHTML() {
        Path htmlFilePath = Path.of(LocalPaths.HTML_POST_CODE_PATH);
        String htmlContent;
        try {
            htmlContent = Files.readString(htmlFilePath);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(htmlContent, HttpStatus.OK);
    }

    private void updateModel(Model model, Code code) {
        model.addAttribute("date", code.getFormattedDate());
        model.addAttribute("code", code.getCode());
        model.addAttribute("id", code.getId());
        model.addAttribute("time", code.getTimeLeft());
        model.addAttribute("views", code.getViews());
    }

    @GetMapping(value = "code/{uuid}", produces = "text/html")
    public String getCodeHtmlByUuid(Model model, @PathVariable UUID uuid) throws NoSuchCodeException {
        Optional<Code> code = codeService.getCodeByUUID(uuid);

        if (code.isEmpty()) {
            throw new NoSuchCodeException();
        }

        Code presentCode = code.get();

        if (presentCode.getViews() == 0 && presentCode.getTimeLeft() == 0 && !presentCode.isViewsRestriction()) {
            updateModel(model, presentCode);
            return "code";
        } else if (presentCode.getViews() > 0 && presentCode.getTimeLeft() > 0) {
            code = codeService.updateCodeViews(presentCode);
            if (code.isPresent()) {
                code = codeService.updateCodeTime(code.get());
                if (code.isPresent()) {
                    updateModel(model, code.get());
                    return "codeTimeViews";
                } else {
                    throw new NoSuchCodeException();
                }
            } else {
                throw new NoSuchCodeException();
            }
        } else if (presentCode.getViews() >= 0 && presentCode.isViewsRestriction()) {
            code = codeService.updateCodeViews(presentCode);
            if (code.isPresent()) {
                updateModel(model, code.get());
                return "codeViews";
            } else {
                throw new NoSuchCodeException();
            }
        } else {
            code = codeService.updateCodeTime(presentCode);
            if (code.isPresent()) {
                updateModel(model, code.get());
                return "codeTime";
            } else {
                throw new NoSuchCodeException();
            }
        }

    }

    @GetMapping(value = "code/latest", produces = "text/html")
    public String getCodeHtmlLatest(Model model) {
        model.addAttribute("codeList", codeService.getCode10Latest());
        return "codeLatest";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class NoSuchCodeException extends Exception {
        public NoSuchCodeException() {
            super("no such code");
        }
    }

}
