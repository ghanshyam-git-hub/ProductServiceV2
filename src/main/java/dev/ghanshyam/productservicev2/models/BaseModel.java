package dev.ghanshyam.productservicev2.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@MappedSuperclass
public class BaseModel implements Serializable { // for redis storage
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uuid;
    boolean isDeleted;
}
