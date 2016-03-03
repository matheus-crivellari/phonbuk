package com.example.matheus.phonebuk;

public class ContactCategory {
    private int id;
    private String name;
    private int idColor;
    private int categoryBelongsToSystem;

    /**
     * Represents a Category to which a Contact will be related to.
     *
     * @param id             Represents the Category's integer id.
     * @param name           Represents the Category's name.
     * @param idColor        Represents the Category's integer color id.
     * @param systemCategory Tells if the Category is protected by the system, that is, if it can be either edited/excluded by the uer or not.
     */
    public ContactCategory(int id, String name, int idColor, int systemCategory) {
        this.id = id;
        this.name = name;
        this.idColor = idColor;
        this.categoryBelongsToSystem = systemCategory;
    }

    /*
    Getters
     */

    /**
     * Gets the Category's id.
     * @return An integer representing the Category's id.
     */
    public int getId() { return id; }

    /**
     * Gets the Category's name.
     * @return A string representing the Category's name.
     */
    public String getName() { return name; }

    /**
     * Gets the Category's color id.
     * @return An integer representing the Color id used to set the Category's color. This id can be translated to the hexadecimal integer by the @see {@link com.example.matheus.phonebuk.Color#getColor(int)} static method.
     */
    public int getIdColor() { return idColor; }

    /**
     * Tells if the Category is either protected by the system or not (1 for protected and 0 for not protected).
     * @return An integer defining if the Category is either protected from edition/deletion by the system (1) or not (0).
     */
    public int getCategoryBelongsToSystem() { return categoryBelongsToSystem; }

    /*
    Setters
     */

    /**
     * Sets the Category's id.
     * @param id An integer representing the Category's id.
     */
    public void setId(int id) { this.id = id; }

    /**
     * Sets the Category's name.
     * @param name A string representing the Category's name.
     */
    public void setName(String name) { this.name = name; }

    /**
     * Sets the Category's color id.
     * @param idColor An integer representing the Color id used to set the Category's color. This id can be translated to the hexadecimal integer by the @see {@link com.example.matheus.phonebuk.Color#getColor(int)}.getColor static method.
     */
    public void setIdColor(int idColor) { this.idColor = idColor; }

    /**
     * Sets if the Category is either protected by the system or not (1 for protected and 0 for not protected).
     * @param categoryBelongsToSystem An integer defining if the Category is either protected from edition/deletion by the system (1) or not (0).
     */
    public void setCategoryBelongsToSystem(int categoryBelongsToSystem) { this.categoryBelongsToSystem = categoryBelongsToSystem; }
}
