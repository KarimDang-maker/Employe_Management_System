package root.store.store.Service;


import root.store.store.DTO.EmployeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import root.store.store.Model.Departement;
import root.store.store.Model.Employe;
import root.store.store.Model.Poste;
import root.store.store.Model.TypePoste;
import org.springframework.stereotype.Service;
import root.store.store.Repository.EmployeRepository;
import root.store.store.Repository.PosteRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor

public class EmployeService {


        private final EmployeRepository employeRepository;
        private final PosteRepository posteRepository;

        // Ajouter un nouvel employe dans la BD
        public Employe addEmploye(Employe employe){
            return employeRepository.save(employe);
        }

    // Recupérer la liste de tous les employes qui existent dans la BD
    public List<EmployeDTO> getAllEmployes(){
        return employeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Recupérer un employé qui existe dans la BD par son ID
    public EmployeDTO getEmployeById(UUID id){
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé !"));
        return mapToDTO(employe);
    }


    // Recupérer la liste des employés par Poste
    public List<EmployeDTO> getAllEmployeesByPoste(UUID id){
        return employeRepository.findEmployeByPoste_Id(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Recupérer la liste des employés par Departement
    public List<EmployeDTO> getAllEmployeesByDepartement(UUID id){
        return employeRepository.findEmployeByDepartement_Id(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Modifier les informations sur un employe (existingEmploye)  dejà existant en BD
    public Employe updateEmploye(UUID id, Employe updatedEmploye) {
        return employeRepository.findById(id).map(
                existingEmploye -> {
                    existingEmploye.setNom(updatedEmploye.getNom());
                    existingEmploye.setPoste(updatedEmploye.getPoste());
                    existingEmploye.setDepartement(updatedEmploye.getDepartement());
                    existingEmploye.setDateEmbauche(updatedEmploye.getDateEmbauche());
                    existingEmploye.setEmail(updatedEmploye.getEmail());
                    existingEmploye.setSalaire(updatedEmploye.getSalaire());

                    return employeRepository.save(existingEmploye);
                }
        ).orElseThrow(() -> new RuntimeException("Employé non trouvé !"));
    }

    // Supprimer de la BD un employe par son ID
    public void deleteEmploye(UUID id) {
        if (!employeRepository.existsById(id)){
            throw new EntityNotFoundException("L'employé avec id " + id + " n'existe pas !");
        }
        employeRepository.deleteById(id);
    }

    // Methode 1 de conversion d'une entité en entiteDTO
    public EmployeDTO mapToDTO(Employe employe) {
        return new EmployeDTO(
                employe.getId(),
                employe.getNom(),
                employe.getEmail(),
                Period.between(employe.getDateEmbauche(), LocalDate.now()).getYears(),
                employe.getSalaire(),
                employe.getPoste().getLibellePoste(),
                employe.getDepartement().getLibelleDepartement()
        );
    }

    // Methode 2 de conversion d'une entité en entiteDTO
/**
 private EmployeDTO convertToDTO(Employe employe){
 EmployeDTO employeDTO = new EmployeDTO();
 employeDTO.setId(employe.getId());
 employeDTO.setNom(employe.getNom());
 employeDTO.setEmail(employe.getEmail());
 employeDTO.setAnciennete(Period.between(employe.getDateEmbauche(),
 LocalDate.now()).getYears());
 employeDTO.setSalaire(employe.getSalaire());
 employeDTO.setPoste(employe.getPoste().getLibellePoste());
 employeDTO.setDepartement(employe.getDepartement().getLibelleDepartement());
 return employeDTO;
 }
 **/
}
