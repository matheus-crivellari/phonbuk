# Phonbuk
A new phone book proposal with a simpler and user-friendly interface.

##Overview
Phonbuk was supposed to be a simpler phone book Android app, where the user can list their Contacts and easily filter them by custom Categories.

It was developed as the Final Project to a Android Fundamentals Course.

##Objectives
The Objective of this project was to supply a phone book easier to use than Android's Contacts with a simpler interface and a very effective usability.


##Classes
###[Color](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/Color.java)
This class is intended to translate app's internal color ids to hexadecimal integers colors.

###[Contact](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/Contact.java)
This class represents a Contact instance. It can be filled with information either from the database or to be saved in it.

###[ContactCategory](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/ContactCategory.java)
This class represents a Category instance to which a Contact can be related to. It can be filled with information either from the database or to be saved in it.

###[ContactDetail](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/ContactDetail.java)
This class represents all of a specific Contact's details. It can be filled with information either from the database or to be saved in it.

###[DataBridge](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/DataBridge.java)
This class is losely based on the singleton pattern and it's used to bridge information and data among all Activities and Classes.

###[DatabaseHelper](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/DatabaseHelper.java)
This class, like its name suggests, is a helper to make easier all database's transaction handling. This class has all essencial methods for Creation, Reading, Updating and Deleting (CRUD) of all data and database itself.

###[SplashActivity](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/SplashActivity.java)
The Activity-named Classes are the classes responsible for handling the app's activities. This one is intended to be the app's Splash Screen and it was made out of a BlankActivity instead of a SplashScreenActivity.

###[ContactsActivity](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/ContactsActivity.java)
This Activity is the main one and the most important. It lists all Contacts and all Categories. From here, the user can get to any other activity, like "Add a Contact" or "Edit a Category".

###[ContactDetailsActivity](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/ContactDetailsActivity.java)
This Activity is responsible for displaying a specific Contact information such as "Name", "Phone Number", "E-mail", "Address" and "Birth Date". From here, the user can easily make a phone call to the Contact, or send an SMS or even send an e-mail.

###[EditContactActivity](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/EditContactActivity.java)
This Activity is responsible for adding a Contact or updating and existing one. It simply changes its behavior based on Extras data passed to it by the Intent.

###[EditCategoryActivity](https://github.com/matheus-crivellari/phonbuk/blob/master/app/src/main/java/com/example/matheus/phonebuk/EditCategoryActivity.java)
This Activity is responsible for adding a Category or updating and existing one. It simply changes its behavior based on Extras data passed to it by the Intent.

##Future Improvements
This project was meant to imitate the basic Android Contact's features. But due to no time for full development some of the features are not finished. They are:

* Multiple phone number's adding to / removing from a single contact;
* Multiple e-mail addresses adding to / removing from a single contact;
* Basic avatar image editing (such as croping, resizing);
* More effective category's tab coloring (some colors make the label impossible to be read due to the incomatibility with the white font color);
* Some performance improvements.