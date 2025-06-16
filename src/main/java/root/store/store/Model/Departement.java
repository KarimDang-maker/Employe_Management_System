package root.store.store.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "Departements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "employes")  // Exclusion de la liste pour éviter boucle infinie
public class Departement {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    @NotBlank(message = "Le libellé du département est obligatoire")
    private String libelleDepartement;

    // ✅ Getter manuel pour garantir l'accès par Thymeleaf
    @Getter
    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    private List<Employe> employes;

}






//package root.store.store.Model;
//
//import jakarta.persistence.*;
//import jakarta.validation.constraints.NotBlank;
//import lombok.*;
//
//import java.util.List;
//import java.util.UUID;
//
//@Entity
//@Table(name = "Departements")
//@Data
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@ToString
//public class Departement {
//
//    @Id
//    @GeneratedValue
//    private UUID id;
//
//    @Column(nullable = false)
//    @NotBlank(message = "Le libellé du département est obligatoire")
//    private String libelleDepartement;
//
//    // ✅ Getter manuel pour garantir l'accès par Thymeleaf
//    @Getter
//    @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
//    private List<Employe> employes;
//
//}
