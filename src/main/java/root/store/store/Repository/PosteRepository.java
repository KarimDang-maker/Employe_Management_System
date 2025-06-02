package root.store.store.Repository;


import root.store.store.Model.Employe;
import root.store.store.Model.Poste;
import root.store.store.Model.TypePoste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface PosteRepository extends JpaRepository<Poste, UUID> {
    // Pour eviter les doublons sur les postes en BD
    boolean existsDistinctByLibellePoste(TypePoste libellePoste);
}