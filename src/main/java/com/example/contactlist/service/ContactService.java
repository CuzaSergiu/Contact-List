package com.example.contactlist.service;

import com.example.contactlist.exception.ResourceNotFoundException;
import com.example.contactlist.model.Contact;
import com.example.contactlist.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;

    @Autowired
    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public boolean existsById(Long id) {
        return contactRepository.existsById(id);
    }

    public Contact findById(Long id) throws ResourceNotFoundException {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            throw new ResourceNotFoundException("Cannot find Contact with id : " + id);
        } else return contact;
    }

    public List<Contact> findAll(int pageNumber, int rowPerPage) {
        List<Contact> contacts = new ArrayList<>();
        Pageable sortedByIdAsc = PageRequest.of(pageNumber - 1, rowPerPage, Sort.by("id").ascending());
        contactRepository.findAll(sortedByIdAsc).forEach(contacts::add);
        return contacts;
    }

    public Contact save(Contact contact) throws ResourceNotFoundException {
        if (!StringUtils.isEmpty(contact.getName())) {
            if (contact.getId() != null && existsById(contact.getId())) {
                throw new ResourceNotFoundException("Contact with id : " + contact.getId() + " already exists");
            }
        }
        return contactRepository.save(contact);
    }

    public void update(Contact contact) throws ResourceNotFoundException {
        if (StringUtils.isEmpty(contact.getName())) {
            if (!existsById(contact.getId())) {
                throw new ResourceNotFoundException("Cannot find Contact with id : " + contact.getId());
            }
            contactRepository.save(contact);
        }
    }

    public void deleteById(Long id) throws ResourceNotFoundException {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Cannot find contact with id " + id);
        } else {
            contactRepository.deleteById(id);
        }
    }

    public Long count() {
        return contactRepository.count();
    }

}
