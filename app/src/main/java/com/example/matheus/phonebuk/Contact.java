package com.example.matheus.phonebuk;

public class Contact {
    private int _id;
    private String name;
    private String address;
    private String birthDate;
    private String image_path;

    /**
     * Represents a Contact.
     */
    public Contact(){ super(); }

    /**
     * Represents a Contact.
     * @param id            Represents the contact's integer id.
     * @param name          Stores the contact's name entered by the user.
     * @param address       Stores the contact's address entered by the user.
     * @param birthDate     Stores the contact's birth date entered by the user.
     * @param image_path    Stores the URL to the contact's image.
     */
    public Contact(int id, String name, String birthDate, String address, String image_path){
        this._id		= id;
        this.name		= name;
        this.address = address;
        this.birthDate = birthDate;
        this.image_path	= image_path;
    }

    /*
    Getters
     */

    /**
     * Gets the Contact's id.
     * @return An integer representing the Contact's id.
     */
    public int    getId() { return _id; }

    /**
     * Gets the Contact's name.
     * @return A string representing the Contact's name.
     */
    public String getName() { return name; }

    /**
     * Gets Contact's address.
     * @return A string representing the Contact's address.
     */
    public String getAddress() { return address; }

    /**
     * Gets the Contact's birth date.
     * @return A string representing the Contact's birth date.
     */
    public String getBirthDate() { return birthDate; }

    /**
     * Gets the path to the Contact's avatar.
     * @return A string representing the relative-to-the-package image's path.
     */
    public String getImagePath() { return image_path; }

    /*
    Setters
     */

    /**
     * Sets the Contact's id.
     * @param _id An integer representing the desired Contact's id.
     */
    public void setId(int _id) { this._id = _id; }

    /**
     * Sets the Contact's name.
     * @param name A string representing the desired Contact's name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the Contact's address.
     * @param address A string representing the desired Contact's address.
     */
    public void setAddress(String address) { this.address = this.address; }

    /**
     * Sets the Contact's birth date.
     * @param birthDate A string representing the desired Contact's birth date.
     */
    public void setBirthdate(String birthDate) { this.birthDate = birthDate; }

    /**
     * Sets the Contact's id.
     * @param image_path A string representing the desired image's path.
     */
    public void setImagePath(String image_path) { this.image_path = image_path; }
}
