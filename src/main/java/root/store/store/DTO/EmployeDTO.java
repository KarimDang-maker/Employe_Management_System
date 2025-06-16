package root.store.store.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import root.store.store.Model.TypePoste;

import java.time.LocalDate;
import java.util.UUID;

public record EmployeDTO(
        UUID id,
        String nom,
        String email,
        Integer anciennete,
        Integer salaire,
        TypePoste poste,
        String departement,
        LocalDate dateEmbauche  // ajouté ici
){}
