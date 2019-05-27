package ru.hse.BikeSharing.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
@Data
public class Alert extends AuditModel {

    @Id
    @GeneratedValue
    Long id;

    String message;
}
