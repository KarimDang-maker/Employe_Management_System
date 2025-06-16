package root.store.store.Controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import root.store.store.Model.Employe;
import root.store.store.Service.DepartementService;
import root.store.store.Service.EmployeService;
import root.store.store.Service.PosteService;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employe")
public class EmployeController {

    private final EmployeService employeService;
    private final DepartementService departementService;
    private final PosteService posteService;

    // Liste de tous les employes
    @GetMapping
    public String employes(Model model) {
        model.addAttribute("employes", employeService.getAllEmployes());
        return "employe/list";
    }

    // Formulaire pour créer un nouvel employe
    @GetMapping("/new")
    public String addEmploye(Model model) {
        model.addAttribute("employe", new Employe());
        model.addAttribute("postes", posteService.getAllPostes());
        model.addAttribute("departements", departementService.getAllDepartements());
        return "employe/add";
    }

    // Sauvegarde des données saisies du formulaire de création
    @PostMapping("/save")
    public String saveEmploye(@Valid @ModelAttribute("employe") Employe employe,
                              BindingResult bindingResult,
                              Model model,
                              RedirectAttributes ra) {
        model.addAttribute("departements", departementService.getAllDepartements());
        model.addAttribute("postes", posteService.getAllPostes());

        if (bindingResult.hasErrors()) {
            return "employe/add";
        }

        try {
            employeService.addEmploye(employe);
            String msg = "L'employé " + employe.getNom() + " a été créé avec succès !";
            ra.addFlashAttribute("msg", msg);
            return "redirect:/employe";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("nom", null, e.getMessage());
            return "employe/add";
        }
    }

    // Afficher les détails d'un employé
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable UUID id, Model model) {
        model.addAttribute("employe", employeService.getEmployeById(id));
        return "employe/detail";
    }

    // Formulaire pour modifier un employé
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model) {
        model.addAttribute("employe", employeService.getEmployeById(id));
        model.addAttribute("postes", posteService.getAllPostes());
        model.addAttribute("departements", departementService.getAllDepartements());
        return "employe/edit";
    }

    // Sauvegarde des données saisies du formulaire de modification
    @PostMapping("/update/{id}")
    public String update(@PathVariable UUID id,
                         @Valid @ModelAttribute("employe") Employe employe,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        model.addAttribute("postes", posteService.getAllPostes());
        model.addAttribute("departements", departementService.getAllDepartements());

        if (bindingResult.hasErrors()) {
            return "employe/edit";
        }

        try {
            employeService.updateEmploye(id, employe);
            String msg = "L'employé " + employe.getNom() + " a été modifié avec succès !";
            ra.addFlashAttribute("msg", msg);
            return "redirect:/employes";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("nom", null, e.getMessage());
            return "employe/edit";
        }
    }

    // Suppression d'un employé
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, RedirectAttributes ra) {
        employeService.deleteEmploye(id);
        String msg = "L'employé a été supprimé avec succès !";
        ra.addFlashAttribute("msg", msg);
        return "redirect:/employe";
    }
}
