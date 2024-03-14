package sameuelesimeone.FitWell.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sameuelesimeone.FitWell.dto.StatDDTO;
import sameuelesimeone.FitWell.dto.StatWDTO;
import sameuelesimeone.FitWell.models.NoteBook.NoteBookD;
import sameuelesimeone.FitWell.models.NoteBook.NoteBookW;
import sameuelesimeone.FitWell.models.User;
import sameuelesimeone.FitWell.services.NoteBookDService;
import sameuelesimeone.FitWell.services.NoteBookWService;

import java.util.UUID;

@RestController
@RequestMapping("/noteBook")
public class NoteBookController {

    @Autowired
    NoteBookWService noteBookWService;

    @Autowired
    NoteBookDService noteBookDService;

    @PatchMapping("/gym/{noteBookWId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public NoteBookW updateStat(@PathVariable UUID noteBookWId, @AuthenticationPrincipal User user, StatWDTO statWDTO){
        return noteBookWService.modNote(noteBookWId, user, statWDTO);
    }

    @PatchMapping("/diet/{noteBookDId}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public NoteBookD updateStat(@PathVariable UUID noteBookDId, @AuthenticationPrincipal User user, StatDDTO statDDTO){
        return noteBookDService.modNote(noteBookDId, user, statDDTO);
    }

}
