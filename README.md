# Phonbuk
A new phone book proposal with a simpler and user-friendly interface.

##Overview
Phonbuk was supposed to be a simpler phone book Android app, where the user can list their Contacts and easily filter them by custom Categories.

It was developed as the Final Project to a Android Fundamentals Course.

##Objectives
The Objective of this project was to supply a phone book easier to use than Android's Contacts with a simpler interface and a very effective usability.


##Classes
###Color
This class is intended to translate app's internal color ids to hexadecimal integers colors.

###Contact
This class represents a Contact instance. It can be filled with information either from the database or to be saved in it.

###ContactCategory
This class represents a Category instance to which a Contact can be related to. It can be filled with information either from the database or to be saved in it.

###ContactDetail
This class represents all of a specific Contact's details. It can be filled with information either from the database or to be saved in it.

###DataBridge
This class is losely based on the singleton pattern and it's used to bridge information and data among all Activities and Classes.

###DatabaseHelper
This class, like its name suggests, is a helper to make easier all database's transaction handling. This class has all essencial methods for Creation, Reading, Updating and Deleting (CRUD) of all data and database itself.

###SplashActivity
The Activity-named Classes are the classes responsible for handling the app's activities. This one is intended to be the app's Splash Screen and it was made out of a BlankActivity instead of a SplashScreenActivity.

###ContactsActivity
This Activity is the main one and the most important. It lists all Contacts and all Categories. From here, the user can get to any other activity, like "Add a Contact" or "Edit a Category".

###ContactDetailsActivity
This Activity is responsible for displaying a specific Contact information such as "Name", "Phone Number", "E-mail", "Address" and "Birth Date". From here, the user can easily make a phone call to the Contact, or send an SMS or even send an e-mail.

###EditContactActivity
This Activity is responsible for adding a Contact or updating and existing one. It simply changes its behavior based on Extras data passed to it by the Intent.

###EditCategoryActivity
This Activity is responsible for adding a Category or updating and existing one. It simply changes its behavior based on Extras data passed to it by the Intent.