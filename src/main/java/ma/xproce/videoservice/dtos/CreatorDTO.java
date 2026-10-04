package ma.xproce.videoservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatorDTO {
    private Long id;
    private String name;
    private String email;
}