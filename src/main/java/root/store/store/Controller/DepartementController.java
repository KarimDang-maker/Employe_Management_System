package root.store.store.Controller;

import root.store.store.DTO.EmployeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import root.store.store.Model.Departement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import root.store.store.Service.DepartementService;
import root.store.store.Service.EmployeService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departement")
public class DepartementController {

    private final DepartementService departementService;
    private final EmployeService employeService;

    // Liste des départements
    @GetMapping
    public String getAllDepartements(Model model) {
        model.addAttribute("departemnts", departementService.getAllDepartements());
        return "departement/list";
    }

    // Afficher le formulaire de création d’un département
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("departement", new Departement());
        return "departement/add";
    }

    // Enregistrer un nouveau département
    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("departement") Departement departement,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "departement/add";
        }

        try {
            departementService.addDepartement(departement);
            String msg = "Le département " + departement.getLibelleDepartement() + " a été créé avec succès.";
            ra.addFlashAttribute("msg", msg);
            return "redirect:/departement";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("libelleDepartement", null, e.getMessage());
            return "departement/add";
        }
    }

    // Liste des employés d’un département
    @GetMapping("/employes/{id}")
    public String listeEmployesByPoste(@PathVariable UUID id, Model model) {
        Departement departement = departementService.getDepartementById(id)
                .orElseThrow(() -> new EntityNotFoundException("Département non existant"));

        List<EmployeDTO> employesDTO = employeService.getAllEmployeesByDepartement(id);
        model.addAttribute("departement", departement.getLibelleDepartement());
        model.addAttribute("employes", employesDTO);

        return "departement/listemploye";
    }

    // Afficher le formulaire de modification d’un département
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        Departement departement = departementService.getDepartementById(id)
                .orElseThrow(() -> new EntityNotFoundException("Département non trouvé"));
        model.addAttribute("departement", departement);
        return "departement/edit";
    }

    // Mettre à jour un département existant
    @PostMapping("/update/{id}")
    public String update(@PathVariable UUID id,
                         @Valid @ModelAttribute("departement") Departement departement,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "departement/edit";
        }

        try {
            departementService.updateDepartement(id, departement);
            String msg = "Le département " + departement.getLibelleDepartement() + " a été modifié avec succès.";
            ra.addFlashAttribute("msg", msg);
            return "redirect:/departement";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("libelleDepartement", null, e.getMessage());
            return "departement/edit";
        }
    }

    // Supprimer un département
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        departementService.deleteDepartement(id);
        String msg = "Le département a été supprimé avec succès !";
        ra.addFlashAttribute("msg", msg);
        return "redirect:/departement";
    }
}
