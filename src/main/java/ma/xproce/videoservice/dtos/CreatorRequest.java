package ma.xproce.videoservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder; // <-- Ajouter cet import
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // <-- Ajouter cette annotation
public class CreatorRequest {
    private String name;
    private String email;
}