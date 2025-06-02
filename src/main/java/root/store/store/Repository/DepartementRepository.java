package root.store.store.Repository;

import root.store.store.Model.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

// JpaRepository<Departement, UUID> Departement = Nom entite et UUID = type de la clef prima

@Repository
public interface DepartementRepository extends JpaRepository<Departement, UUID> {
    // Pour eviter les doublons sur les departements en BD
    boolean existsDistinctByLibelleDepartement(String libelleDepartement);
}