package com.ttknpdev.understandjwtmysqlmanytomany.entities.relations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    // assume I have fixed roles
    private Long rid;
    @Column
    private String name;
}
