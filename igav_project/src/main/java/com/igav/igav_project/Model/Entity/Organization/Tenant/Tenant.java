package com.igav.igav_project.Model.Entity.Organization.Tenant;

import jakarta.persistence.*;

@Entity 
@Table (name = "tenants")
public class Tenant {

    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    public String Nombre;
    public String SuscriptionActualId;
    


}
