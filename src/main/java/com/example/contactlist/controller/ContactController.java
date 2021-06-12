package com.example.contactlist.controller;

import com.example.contactlist.exception.ResourceNotFoundException;
import com.example.contactlist.model.Contact;
import com.example.contactlist.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
public class ContactController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final int ROW_PER_PAGE = 5;

    private final ContactService contactService;

    @Autowired
    public ContactController(ContactService contactService) {
        this.contactService = contactService;
    }

    @Value("${msg.title}")
    private String title;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        log.info("index page");
        model.addAttribute("title", title);
        return "index";
    }

    @GetMapping(value = "/contacts")
    public String getContacts(Model model, @RequestParam(value = "page", defaultValue = "1") int pageNumber) {
        List<Contact> contacts = contactService.findAll(pageNumber, ROW_PER_PAGE);

        long count = contactService.count();
        boolean hasPrev = pageNumber > 1;
        boolean hasNext = (pageNumber * ROW_PER_PAGE) < count;
        model.addAttribute("contacts", contacts);
        model.addAttribute("hasPrev", hasPrev);
        model.addAttribute("hasNext", hasNext);
        model.addAttribute("next", pageNumber + 1);
        return "contact-list";

    }

    //
//    @GetMapping(value = "/contacts/{contactId}")
//    public String getContactById(Model model, @PathVariable long contactId) {
//
//    }
//
    @GetMapping(value = "/contacts/add")
    public String showAddContact(Model model) {
        log.info("showAddContact getMapping called");
        Contact contact = new Contact();
        model.addAttribute("add", true);
        model.addAttribute("contact", contact);

        return "contact-edit";
    }

    @PostMapping(value = "/contacts/add")
    public String addContact(Model model, @ModelAttribute("contact") Contact contact) {
            //todo de ce nu redirectioneaza corect pagina cand adaugi contact sau sa editezi
        try {
            Contact newContact = contactService.save(contact);
            log.info("addContact post method called");

            return "redirect:/contacts/" + String.valueOf(newContact.getId());
        } catch (Exception exception) {
            log.info("error in addContact post method");
            String errorMessage = exception.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);

            return "contact-edit";
        }
    }

    @GetMapping(value = "/contacts/{contactId}/edit")
    public String showEditContact(Model model, @PathVariable long contactId) {
        Contact contact = null;
        try {
            contact = contactService.findById(contactId);
        } catch (ResourceNotFoundException ex) {
            model.addAttribute("errorMessage", "Contact not found");
        }
        model.addAttribute("add", false);
        model.addAttribute("contact", contact);
        return "contact-edit";
    }

    @PostMapping(value = "/contacts/{contactId}/edit")
    public String updateContact(Model model,
                                @PathVariable long contactId,
                                @ModelAttribute("contact") Contact contact) {
        try {
            contact.setId(contactId);
            contactService.update(contact);
            log.info("updateContact post method called, contact updated");
            return "redirect:/contacts/" + String.valueOf(contact.getId());

        } catch (Exception exception) {
            String errorMessage = exception.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", false);

            return "contact-edit";
        }


    }
//
//    @GetMapping(value = "/contacts/{contactId}/delete")
//    public String showDeleteContactById(Model model, @PathVariable long contactId) {
//
//    }
//
//    @PostMapping(value = "/contacts/{contactId}/delete")
//    public String deleteContactById(Model model, @PathVariable long contactId) {
//
//    }


}
