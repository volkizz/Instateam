package com.nago.instateam.web.controller;

import com.nago.instateam.model.Collaborator;
import com.nago.instateam.model.Project;
import com.nago.instateam.model.Role;
import com.nago.instateam.service.CollaboratorService;
import com.nago.instateam.service.ProjectService;
import com.nago.instateam.service.RoleService;
import com.nago.instateam.web.FlashMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import javax.validation.Valid;

@Controller
public class RoleController {
  @Autowired
  private RoleService roleService;

  @Autowired
  private ProjectService projectService;

  @Autowired
  private CollaboratorService collaboratorService;

  @SuppressWarnings("unchecked")
  @RequestMapping("/roles")
  public String listCategories(Model model) {

    List<Role> roles = roleService.findAll();

    if(!model.containsAttribute("role")){
      model.addAttribute("role", new Role());
    }
    model.addAttribute("roles", roles);
    return "role/index";
  }

  @RequestMapping(value = "/roles", method = RequestMethod.POST)
  public String addRole(@Valid Role role, BindingResult result, RedirectAttributes redirectAttributes){
    if (result.hasErrors()){
      redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.role", result);
      redirectAttributes.addFlashAttribute("role", role);

      return "redirect:/roles";
    }
    roleService.save(role);

    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role saved!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/roles";
  }

  @RequestMapping(value = "/role/{id}")
  public String editRole(Model model, @PathVariable Long id) {
    Role role= roleService.findById(id);
    if (!model.containsAttribute("role")){
      model.addAttribute("role", role);
    }

    return "/role/edit";
  }

  @RequestMapping(value = "/role/update", method = RequestMethod.POST)
  public String updateCollaborator(@Valid Role role, BindingResult result,
                                   RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.role", result);
      redirectAttributes.addFlashAttribute("role", role);
      return String.format("redirect:/role/%s", role.getId());
    }

    roleService.save(role);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role updated!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/roles";
  }

  @RequestMapping(value = "roles/{id}/delete", method = RequestMethod.POST)
  public String deleteRole(@PathVariable Long id, RedirectAttributes redirectAttributes){
    Role role = roleService.findById(id);
    List<Project> projects = projectService.findAll();
    List<Collaborator> collaborators = collaboratorService.findAll();

    for (Project project : projects){
      project.removeRole(role);
      projectService.save(project);
    }

    for (Collaborator collaborator : collaborators){
      if(Objects.equals(collaborator.getRole(), role)){
        collaborator.setRole(roleService.findById((long) 1)); // "1" is "Unassigned" role id
      }
      collaboratorService.save(collaborator);
    }
    role.setCollaborators(null);
    roleService.save(role);
    roleService.delete(role);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Role deleted!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/roles";
  }
}
