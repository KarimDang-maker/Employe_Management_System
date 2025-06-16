package root.store.store.Controller;


import root.store.store.DTO.EmployeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import root.store.store.Model.Poste;
import root.store.store.Model.TypePoste;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import root.store.store.Service.EmployeService;
import root.store.store.Service.PosteService;

import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
@RequestMapping("/poste")

public class PosteController {

    private final PosteService posteService;
    private final EmployeService employeService;

    // Liste des postes
    @GetMapping
    public String listPostes(Model model) {
        List<Poste> postes = posteService.getAllPostes();
        model.addAttribute("postes", postes);
        return "poste/listposte"; // Removed "templates/" prefix
    }
    // Créer un nouveau poste
    @GetMapping("/new")
    public String showAddPosteForm(Model model) {
        model.addAttribute("poste", new Poste());  // Changed attribute name
        model.addAttribute("types", TypePoste.values());
        return "poste/addposte";  // Removed "templates/" prefix
    }

    @PostMapping("/save")
    public String savePoste(@Valid @ModelAttribute("poste") Poste poste,
                            BindingResult bindingResult,
                            Model model,
                            RedirectAttributes ra) {
        model.addAttribute("types", TypePoste.values());

        // On teste si les données saisies dans le formulaire sont valides
        if (bindingResult.hasErrors())
        {
            return "poste/addposte"; // Retourner à la vue avec les erreurs
        }

        try
        {
            posteService.addPoste(poste);
    // Message flash
            String msg = "Le poste " + poste.getLibellePoste() +
                    " a été créé avec succès !";
            ra.addFlashAttribute("msg", msg);

    /** Après la création d'un nouveau poste on se redirige
        vers la page des listes des postes */
            return "redirect:/poste";

        } catch (IllegalArgumentException e)
        {
            bindingResult.rejectValue("libellePoste", null, e.getMessage());
            return "poste/addposte";
        }

    }

    // Pour voir la liste des employés pour le poste indiqué
    @GetMapping("/employes/{id}")
    public String listeEmployesByPoste(@PathVariable UUID id, Model model)
    {
        Poste poste = posteService.getPosteById(id)
                .orElseThrow(() -> new EntityNotFoundException("Poste non existant !"));
    // Liste des employes du poste dont l'id est {id}
        List<EmployeDTO> employesDTO = employeService.getAllEmployeesByPoste(id);

    // Transmission des variables au template
        model.addAttribute("templates/poste", poste.getLibellePoste());
        model.addAttribute("employes", employesDTO);
    // Affichage du template
        return "poste/listeemployeposte";
    }


    // Modifier les informations d'un poste
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model, RedirectAttributes ra) {
    // On recupère le poste à modifier
        Poste poste = posteService.getPosteById(id)
                .orElseThrow(() -> new EntityNotFoundException("Poste non trouvé"));

        model.addAttribute("types", TypePoste.values());
        model.addAttribute("poste", poste);

        // Appel de la page qui affichera les informations du poste à modifier
        return "poste/editposte";
    }

    @PostMapping("/update/{id}")
    public String update(
            @PathVariable UUID id,
            @Valid @ModelAttribute("poste") Poste poste,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes ra) {

        model.addAttribute("types", TypePoste.values());

        if (bindingResult.hasErrors()) {
            model.addAttribute("poste", poste);
            model.addAttribute("types", TypePoste.values());
            return "poste/editposte";
        }

        try {
            posteService.updatePoste(id, poste);

            String msg = "Le poste " + poste.getLibellePoste() + " a été mis à jour avec succès !";
            ra.addFlashAttribute("msg", msg);

            return "redirect:/poste";

        } catch (EntityNotFoundException e) {
            // Si le poste n'existe pas, on redirige avec un message d'erreur
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/poste";

        } catch (IllegalArgumentException e) {
            // Si une autre validation métier échoue (ex : libellé dupliqué)
            bindingResult.rejectValue("libellePoste", null, e.getMessage());
            model.addAttribute("poste", poste);
            return "poste/editposte";
        }
    }
        // Supprimer un poste de la BD

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        try {
            posteService.deletePoste(id);

            // Message flash en cas de succès
            String msg = "Le poste a été supprimé avec succès !";
            ra.addFlashAttribute("msg", msg);

        } catch (EntityNotFoundException e) {
            // Message flash en cas d'erreur
            ra.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/poste";
    }
}


