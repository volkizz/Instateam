package com.nago.instateam.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull
  @Size(min = 1, max = 26)
  private String name;

  @NotNull
  @Size(min = 3, max = 1024)
  private String description;

  private String status;

  @Column(updatable = false)
  private Date startDate = new Date();

  @ManyToMany(cascade = {CascadeType.ALL})
  @JoinTable(name = "PROJECT_ROLE",
      joinColumns = {@JoinColumn(name = "ROLESNEEDED_ID")},
      inverseJoinColumns = {@JoinColumn(name = "PROJECT_ID")})
  private List<Role> rolesNeeded = new ArrayList<>();

  @ManyToMany
  @JoinTable(name = "PROJECT_COLLABORATOR",
      joinColumns = {@JoinColumn(name = "COLLABORATORS_ID")},
      inverseJoinColumns = {@JoinColumn(name = "PROJECT_ID")})
  private List<Collaborator> collaborators = new ArrayList<>();

  public Project() {
  }

  public Long getId() {return id;}

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Date getStartDate() {return startDate;}

  public List<Role> getRolesNeeded() {
    return rolesNeeded;
  }

  public void setRolesNeeded(List<Role> rolesNeeded) {
    this.rolesNeeded = rolesNeeded;
  }

  public List<Collaborator> getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(List<Collaborator> collaborators) {
    this.collaborators = collaborators;
  }

  public void removeCollaborator(Collaborator collaborator) {
    collaborators.remove(collaborator);
  }

  public void removeRole(Role role) {
    rolesNeeded.remove(role);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Project project = (Project) o;

    if (id != null ? !id.equals(project.id) : project.id != null) {
      return false;
    }
    if (name != null ? !name.equals(project.name) : project.name != null) {
      return false;
    }
    if (description != null ? !description.equals(project.description)
        : project.description != null) {
      return false;
    }
    if (status != null ? !status.equals(project.status) : project.status != null) {
      return false;
    }
    if (startDate != null ? !startDate.equals(project.startDate) : project.startDate != null) {
      return false;
    }
    if (rolesNeeded != null ? !rolesNeeded.equals(project.rolesNeeded)
        : project.rolesNeeded != null) {
      return false;
    }
    return collaborators != null ? collaborators.equals(project.collaborators)
        : project.collaborators == null;

  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + (description != null ? description.hashCode() : 0);
    result = 31 * result + (status != null ? status.hashCode() : 0);
    result = 31 * result + (startDate != null ? startDate.hashCode() : 0);
    result = 31 * result + (rolesNeeded != null ? rolesNeeded.hashCode() : 0);
    result = 31 * result + (collaborators != null ? collaborators.hashCode() : 0);
    return result;
  }

  public static Comparator<Project>
      projectComparator =
      (p1, p2) -> p1.getStartDate().compareTo(p2.getStartDate());
}
