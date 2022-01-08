package account.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Payroll {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @NotBlank
    private String employee;
    @NotBlank
    @Pattern(regexp = "(0[1-9]|10|11|12)-20[0-9]{2}")
//    @Column(unique=true)
    private String period;
    @Min(value = 0)
    @NotNull(message = "Please enter id")
    private Long salary;

    private Long userid;





//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "user")
//    @JsonBackReference
//    private User user;


}
