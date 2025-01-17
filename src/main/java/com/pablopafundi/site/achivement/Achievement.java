package com.pablopafundi.site.achivement;


import com.pablopafundi.site.common.domain.BaseWithLang;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "id_achievement"))
@EqualsAndHashCode(callSuper = true)
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "achievement", indexes = {@Index(name = "idx_lang", columnList = "lang")})
public class Achievement extends BaseWithLang {

    @NotEmpty
    @Column(unique = true, nullable = false)
    private String name;


    @NotEmpty
    @Column(unique = true, nullable = false, length = 750)
    private String description;


}
