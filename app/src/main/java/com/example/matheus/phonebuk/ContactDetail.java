package com.example.matheus.phonebuk;

public class ContactDetail extends Contact{
    private int emailId;
    private String email;

    private int telId;
    private String phoneNumber;

    /**
     * Represents a detailed Contact's view to be either displayed to the user or stored in the database.
     * @param id          Represents the Contact's id.
     * @param name        Represents the Contact's name.
     * @param birthDate   Represents the Contact's birth date.
     * @param address     Represents the Contact's address.
     * @param imagePath   Represents the Contact's image path.
     * @param email       Represents the Contact's e-mail address.
     * @param phoneNumber Represents the Contact's phone number.
     */
    public ContactDetail(int id, String name, String birthDate, String address, String imagePath, String email, String phoneNumber){
        super(id,name,birthDate,address,imagePath);

        this.email       = email;
        this.phoneNumber = phoneNumber;
    }

    /*
    Getters
     */

    /**
     * Gets the Contact's e-mail's id.
     * @return An integer representing the Contact's e-mail's id.
     */
    public int getEmailId() { return emailId; }

    /**
     * Gets the Contact's e-mail's address.
     * @return A string representing the Contact's e-mail's address.
     */
    public String getEmail() { return email; }

    /**
     * Gets the Contact's phone's id.
     * @return An integer representing the Contact's phone's id.
     */
    public int getTelId(){ return telId; }

    /**
     * Gets the Contact's phone's number.
     * @return A string representing the Contact's phone's number.
     */
    public String getPhoneNumber() { return phoneNumber; }

    /*
    Setters
     */

    /**
     * Sets the Contact's e-mail's id.
     * @param id An integer representing the desired Contact's e-mail's id.
     */
    public void setEmailId(int id){ this.emailId = id; }

    /**
     * Sets the Contact's e-mail's address.
     * @param email A string representing the desired Contact's e-mail's address.
     */
    public void setEmail(String email) { this.email = email; }

    /**
     * Sets the Contact's phone's id.
     * @param id An integer representing the desired Contact's phone's id.
     */
    public void setTelId(int id){ this.telId = id; }

    /**
     * Sets the Contact's phone's number.
     * @param phoneNumber A string representing the desired Contact's phone's number.
     */
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
