package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;
@MappedSuperclass
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;
    boolean isDeleted;
}
