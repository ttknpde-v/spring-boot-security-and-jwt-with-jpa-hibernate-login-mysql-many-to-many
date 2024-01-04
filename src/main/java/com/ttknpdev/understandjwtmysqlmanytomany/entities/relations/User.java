package com.ttknpdev.understandjwtmysqlmanytomany.entities.relations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
/*
 Let's use JPA annotations to establish MANY-to-MANY relationships
 between User and Role entities
 it's meaning you query with join keyword
*/
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER) // , cascade = CascadeType.ALL specifies that when a Customer is created, if there is any Address association, then that Address will be created as well , So this case I don't need it
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "uid", referencedColumnName = "uid"),
            inverseJoinColumns = @JoinColumn(name = "rid", referencedColumnName = "rid")
    )
    @Column
    private Set<Role> roles;

}