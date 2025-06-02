package root.store.store.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "Departements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Departement {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Le libellé du département est obligatoire")
    private String libelleDepartement;

}