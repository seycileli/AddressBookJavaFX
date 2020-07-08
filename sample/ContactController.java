package sample;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import sample.datamodel.Contact;

import java.util.Optional;

public class ContactController {
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField notesField;

    public Contact getNewContact() {
        /* Declaring all of the fields, which were assigned to the fx:id
        * and then we are */
        Optional<Contact> info;

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String phoneNumber = phoneNumberField.getText();
        String notes = notesField.getText();

        Contact newContact = new Contact(firstName, lastName, phoneNumber, notes);
        return newContact;
    }

    public void editContact(Contact contact) {
        /*
        * For this Method, because we're editing
        * we want to set the new values
        * so we'll use setText() method
        * and within the parems we're
        * calling getters from the encapsulated class Contact
        *
        * Which is how we're able to edit our Contact information */
        firstNameField.setText(contact.getFirstName());
        lastNameField.setText(contact.getLastName());
        phoneNumberField.setText(contact.getPhoneNumber());
        notesField.setText(contact.getNotes());
    }

    public void updateContact(Contact contact) {
        /*
        * For this Method, because we're updating,
        * we'll SET the new values from the previous values
        *
        * This method is responsible for updating our edit contact method */
        contact.setFirstName(firstNameField.getText());
        contact.setLastName(lastNameField.getText());
        contact.setPhoneNumber(phoneNumberField.getText());
        notesField.setText(notesField.getText());
    }

    public void deleteContact(Contact contact) {
        
    }
}
