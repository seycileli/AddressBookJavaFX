package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import sample.datamodel.Contact;
import sample.datamodel.ContactData;

import java.io.IOException;
import java.util.Optional;

public class Controller {
    @FXML
    private BorderPane mainPanel; //ID to the main.fxml

    @FXML
    private ContactData data;
    //creating an Instance of our ContactData class

    /* After creating ContactData, we'll have/ want to initialize
    * the tables data, we've created an fx:id in the tables view
    * but now we'll have to declare the fields */
    @FXML
    private TableView<Contact> contactsTable;

    public void initialize() {
        data = new ContactData();
        data.loadContacts(); //loading contacts
        contactsTable.setItems(data.getContacts()); //initializing contactsTable here
    }

    @FXML
    public void addNewContactHandle() {
        /*
        * This method is assigned to:
        * newcontactwindow.fxml
        *
        * Further Explanation:
        *
        * Created a Dialog instance,
        * and assigning the main.fxml as it's parent
        *
        * We retrieve the Parent from the BorderPane
        * we set the dialog title
        * we then load the FXML file,
        * and we direct the FXML to the FXML we want it to,
        * by placing the name of the FXML file in getResource() method below
        *
        * that FXMl file will then pop up, and give the user the choice
        * to enter in a new contacts information
        * such as first/ last name, phone number and a note
        *
        * and then we added an OK and CANCEL button
        * at the end of this method, for the new contact window fxml
        * */

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Create New Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newcontactwindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            /* Once ContactController Class has been created, we want to know SAVE
            * the Data. So to do this,
            * We'll have to first access the Controller, so we'll assign
            * contactController variable to fxmlLoader.getController,
            * which will be retrieving FXML newcontactwindow.fxml file */
            ContactController contactController = fxmlLoader.getController();
            Contact newContact = contactController.getNewContact();
            //now we'll assign contact to the new contactController
            data.addContact(newContact);
            data.saveContacts();
            //then we'll go on and SAVE the newContact to our Data Class

        }
    }

    @FXML
    public void showEditContactHandle() {
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if(selectedContact == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No contact selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you'll wish to edit.");
            alert.showAndWait();
            return;
        }

        /* What's going on here?
        *
        *  First, we're updating the title of the contact
        *
        *  Second, instead of getting a reference to the ContactController Class
        *  when the user presses the "OK" button,
        *
        *  We're getting it up front instead because we want the user to populate
        *  the Dialog Values with the existing value of the Contact
        *
        *  To do so, we're calling the Edit button/ editContact
        *
        *  Lastly, if the user presses "OK"
        *  we call the updateContact method from the
        *  Controller again */
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainPanel.getScene().getWindow());
        dialog.setTitle("Edit Contact");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newcontactwindow.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Failure to load.");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        ContactController contactController = fxmlLoader.getController();
        contactController.editContact(selectedContact);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            contactController.updateContact(selectedContact);
            data.saveContacts();
        }
    }

    @FXML
    public void deleteContactHandle() {
        /* If user wishes to delete a Contact
        *  but does not have an item/ Contact selected
        *  we will alert the User that they don't have a selected
        *  Contact that they wish to delete */
        Contact selectedContact = contactsTable.getSelectionModel().getSelectedItem();
        if (selectedContact != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("No Contact Selected"); //msg if user doesn't have a contact selected
            alert.setHeaderText(null);
            alert.setContentText("Please select the contact you wish to delete.");
            alert.showAndWait();
            return;
        }

        /* If the User has a selected Item/ Contact
        *  that they wish to delete,
        *  a warning msg will pop up, asking for
        *  their confirmation on if they wish to delete */
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Contact");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you wish to delete "
                + selectedContact.getFirstName() + selectedContact.getLastName());

        Optional<ButtonType> result = alert.showAndWait();
        /* Confirmation that user has selected the OK button
        *  and wanting to delete the selected Contact */
        if (result.isPresent() && result.get() == ButtonType.OK) {
            data.deleteContact(selectedContact);
            data.saveContacts();
            /* since we're using Data Binding,
            *  the table will update automatically */
        }
    }
}
