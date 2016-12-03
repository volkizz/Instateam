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
import javax.validation.Valid;

@Controller
public class CollaboratorController {
  @Autowired
  private ProjectService projectService;

  @Autowired
  private CollaboratorService collaboratorService;

  @Autowired
  private RoleService roleService;

  @SuppressWarnings("unchecked")
  @RequestMapping("/collaborators")
  public String listCategories(Model model) {

    List<Collaborator> collaborators = collaboratorService.findAll();
    List<Role> roles = roleService.findAll();

    if (!model.containsAttribute("collaborator")) {
      model.addAttribute("collaborator", new Collaborator());
    }
    model.addAttribute("collaborators", collaborators);
    model.addAttribute("roles", roles);

    return "collaborator/index";
  }

  @RequestMapping(value = "/collaborators", method = RequestMethod.POST)
  public String addCollaborator(@Valid Collaborator collaborator, BindingResult result,
                                RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.collaborator", result);
      redirectAttributes.addFlashAttribute("collaborator", collaborator);
      return "redirect:/collaborators";
    }
    collaboratorService.save(collaborator);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator Added!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/collaborators";
  }

  @RequestMapping(value = "/collaborators/{id}")
  public String editCollaborator(Model model, @PathVariable Long id) {
    Collaborator collaborator = collaboratorService.findById(id);
    List<Role> roles = roleService.findAll();
    if (!model.containsAttribute("collaborator")){
        model.addAttribute("collaborator", collaborator);
    }
    model.addAttribute("roles", roles);

    return "/collaborator/edit";
  }

  @RequestMapping(value = "/collaborator/update", method = RequestMethod.POST)
  public String updateCollaborator(@Valid Collaborator collaborator, BindingResult result,
                                   RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      redirectAttributes
          .addFlashAttribute("org.springframework.validation.BindingResult.collaborator", result);
      redirectAttributes.addFlashAttribute("collaborator", collaborator);
      return String.format("redirect:/collaborators/%s", collaborator.getId());
    }

    collaboratorService.save(collaborator);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator Updated!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/collaborators";
  }

  @RequestMapping(value = "collaborators/{id}/delete", method = RequestMethod.POST)
  public String deleteCollaborator(@PathVariable Long id, RedirectAttributes redirectAttributes){
    Collaborator collaborator = collaboratorService.findById(id);
    List<Project> allProjects = projectService.findAll();

    for (Project project : allProjects){
      project.removeCollaborator(collaborator);
      projectService.save(project);
    }
    collaborator.setRole(null);
    collaboratorService.save(collaborator);
    collaboratorService.delete(collaborator);
    redirectAttributes.addFlashAttribute("flash", new FlashMessage("Collaborator Deleted!!!", FlashMessage.Status.SUCCESS));
    return "redirect:/collaborators";
  }
}
