package com.nago.instateam.web.controller;

import static com.nago.instateam.model.Project.projectComparator;

import com.nago.instateam.model.Collaborator;
import com.nago.instateam.model.Project;
import com.nago.instateam.model.Role;
import com.nago.instateam.service.ProjectService;
import com.nago.instateam.service.RoleService;
import com.nago.instateam.web.FlashMessage;
import com.nago.instateam.web.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;

@Controller
public class ProjectController {
  @Autowired
  private ProjectService projectService;

  @Autowired
  private RoleService roleService;

  @SuppressWarnings("unchecked")
  @RequestMapping(value = {"/","/projects"})
  public String listCategories(Model model) {
    List<Project> projects = projectService.findAll();

    projects.sort(projectComparator);
    model.addAttribute("projects", projects);
    return "project/index";
  }

  @RequestMapping("/projects/{id}")
  public String project(@PathVariable Long id, Model model) {
    Project project = projectService.findById(id);
    Map<Role, Collaborator> rolesAssignment = getRoleCollaboratorMap(project);

    model.addAttribute("project", project);
    model.addAttribute("rolesAssignment", rolesAssignment);
    return "project/project_detail";
  }

  @RequestMapping("/projects/add")
  public String formNewProject(Model model) {
    List<Role> roles = roleService.findAll();

    if (!model.containsAttribute("project")) {
      model.addAttribute("project", new Project());
    }
    model.addAttribute("action", "/projects");
    model.addAttribute("heading", "New Project");
    model.addAttribute("submit", "Save");
    model.addAttribute("roles", roles);
    model.addAttribute("statuses", Status.values());
    return "project/form";
  }

  @RequestMapping("/projects/{id}/edit")
  public String formEditProject(@PathVariable Long id, Model model) {
    List<Role> roles = roleService.findAll();

    if (!model.containsAttribute("project")) {
      model.addAttribute("project", projectService.findById(id));
    }
    model.addAttribute("roles", roles);
    model.addAttribute("statuses", Status.values());
    model.addAttribute("action", String.format("/projects/%s", id));
    model.addAttribute("heading", "Edit Project");
    model.addAttribute("submit", "Update");
    return "project/form";
  }

  @RequestMapping(value = "/projects/{id}", method = RequestMethod.POST)
  public String updateProject(@Valid Project project, BindingResult result, RedirectAttributes redirectAttributes){
    if (result.hasErrors()) {
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.project", result);
      redirectAttributes.addFlashAttribute("project", project);
      return String.format("redirect:/projects/%s/edit", project.getId());
    }
    projectService.save(project);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project Updated!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/projects";
  }

  @RequestMapping(value = "/projects", method = RequestMethod.POST)
  public String addProject(@Valid Project project, BindingResult result,
                           RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.project", result);
      redirectAttributes.addFlashAttribute("project", project);
      return "redirect:/projects/add";
    }
    projectService.save(project);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("New Project Created!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/projects";
  }

  @RequestMapping("/projects/{id}/collaborators")
  public String editCollaborators(Model model, @PathVariable Long id) {
    Project project = projectService.findById(id);

    model.addAttribute("project", project);

    return "project/project_collaborators";
  }

  @RequestMapping(value = "/projects/{id}/collaborators/update", method = RequestMethod.POST)
  public String assignCollaborators(@PathVariable Long id,
                                    Project project, RedirectAttributes redirectAttributes){
    Project savedProject = projectService.findById(id);
    savedProject.setCollaborators(
        project.getCollaborators()
    );
    projectService.save(savedProject);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborators Assigned!!!", FlashMessage.Status.SUCCESS));
    return String.format("redirect:/projects/%s", id);
  }

  @RequestMapping(value = "/projects/{id}/delete", method = RequestMethod.POST)
  public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    Project project = projectService.findById(id);
    List<Collaborator> collaborators = new ArrayList<>(project.getCollaborators());
    project.setRolesNeeded(null);
    collaborators.forEach(project::removeCollaborator);
    projectService.save(project);
    projectService.delete(project);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Project Deleted!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/projects";
  }

  private Map<Role, Collaborator> getRoleCollaboratorMap(Project project) {
    List<Role> rolesNeeded = project.getRolesNeeded();
    List<Collaborator> collaborators = project.getCollaborators();
    Map<Role, Collaborator> roleCollaborator = new LinkedHashMap<>();

    for (Role roleNeeded : rolesNeeded) {
      roleCollaborator.put(roleNeeded,
          collaborators.stream()
              .filter((col) -> col.getRole().getId().equals(roleNeeded.getId()))
              .findFirst()
              .orElseGet(() -> {
                Collaborator unassigned = new Collaborator();
                unassigned.setName("Unassigned");
                return unassigned;
              }));
    }
    return roleCollaborator;
  }
}
