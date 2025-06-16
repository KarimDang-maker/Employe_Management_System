package root.store.store.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import root.store.store.Model.Departement;
import org.springframework.stereotype.Service;
import root.store.store.Repository.DepartementRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DepartementService {

    private final DepartementRepository departementRepository;

    // Ajouter un nouveau département en BD
    public Departement addDepartement(Departement departement) {
        // On vérifie que le département n'existe pas déjà
        if (departementRepository.existsDistinctByLibelleDepartement(departement.getLibelleDepartement())) {
            throw new IllegalArgumentException("Ce département existe déjà !");
        }
        return departementRepository.save(departement);
    }

    // Récupérer la liste de tous les départements dans la BD
    public List<Departement> getAllDepartements() {
        return departementRepository.findAll();
    }

    // Récupérer un département par son ID
    public Optional<Departement> getDepartementById(UUID id) {
        return departementRepository.findById(id);
    }

    // Modifier les informations d’un département existant
    public Departement updateDepartement(UUID id, Departement updatedDepartement) {
        return departementRepository.findById(id).map(existingDepartement -> {
            existingDepartement.setLibelleDepartement(updatedDepartement.getLibelleDepartement());
            return departementRepository.save(existingDepartement);
        }).orElseThrow(() -> new RuntimeException("Département non trouvé !"));
    }

    // Supprimer un département par son ID
    public void deleteDepartement(UUID id) {
        // Si le département à supprimer n'existe pas, afficher un message
        if (!departementRepository.existsById(id)) {
            throw new EntityNotFoundException("Le département avec id " + id + " n'existe pas !");
        }
        departementRepository.deleteById(id);
    }

    // Méthode incomplète à finaliser ou supprimer
    public Object allDepartements() {
        return null; // À compléter selon l’usage réel ou à supprimer
    }
}
