package com.agiletestingdays.untangletestcode.unicornservice.adapter.driven.db;

import com.agiletestingdays.untangletestcode.unicornservice.domain.Unicorn;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Entity
@Table(name = "unicorns")
public class UnicornEntity {

  @Id private UUID id;
  private String name;
  private String maneColor;
  private Integer hornLength;
  private Integer hornDiameter;
  private Instant dateOfBirth;

  public UnicornEntity() {}

  public UnicornEntity(Unicorn unicorn) {
    this.id = unicorn.id();
    this.name = unicorn.name();
    this.maneColor = unicorn.maneColor().name();
    this.hornLength = unicorn.hornLength();
    this.hornDiameter = unicorn.hornDiameter();
    this.dateOfBirth = unicorn.dateOfBirth().atStartOfDay().toInstant(ZoneOffset.UTC);
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getManeColor() {
    return maneColor;
  }

  public void setManeColor(String maneColor) {
    this.maneColor = maneColor;
  }

  public Integer getHornLength() {
    return hornLength;
  }

  public void setHornLength(Integer hornLength) {
    this.hornLength = hornLength;
  }

  public Integer getHornDiameter() {
    return hornDiameter;
  }

  public void setHornDiameter(Integer hornDiameter) {
    this.hornDiameter = hornDiameter;
  }

  public Instant getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Instant dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public Unicorn toUnicorn() {
    return new Unicorn(
        id,
        name,
        Unicorn.ManeColor.valueOf(maneColor),
        hornLength,
        hornDiameter,
        LocalDate.ofInstant(dateOfBirth, ZoneOffset.UTC));
  }
}
