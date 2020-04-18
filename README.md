# ContactsDemo V2

Hi guys. This is the version 2 of the ContactsDemo app. I have made some improvements after everyone's experience with the previous app. I will highlight some of those changes here.

## Blog

Checkout the new blog that explains the cogs in motion for the app:


[Syncing Contacts with an Android Application](https://ajkh35.blogspot.com/2020/04/syncing-contacts-with-android.html)

## Kotlin

I have switched the programming language to Kotlin for this app. I hope this will make the code more concise and easy to read.

## Demo Server

So this app works in tandem with a very simple node server. The server doesn't do much but maintains a list of numbers. This list is synced with the device's numbers via the SyncAdapter.

## Setup

You will need to install nodejs and npm to run the server. Also you need mysql as the database.

### Installation

1. Install mysql from [here](https://dev.mysql.com/downloads/mysql/) 
2. Create a database in mysql.
2. In your database, create a table named "numbers" with a column "number". "number" can be of type varchar(20).
2. Update the app.js file in the demo_server folder to whatever your user, password and database is.
4. Now install nodejs from [here](https://nodejs.org/en/download/)

### Startup

Use command node to start the server on localhost:3000

```bash
cd demo_server
node app.js
```
In case the server does not start, just delete node_modules folder and run

```bash
npm install
```

Then launch the app <br /><br />


### Initially

![MainScreen](/resources/images/SyncContacts1.jpg) ![ContactsScreen](/resources/images/SyncContacts5.jpg) <br /><br />


### Register Number

1. Register number:

![Register](/resources/images/SyncContacts2.jpg) <br /><br />


2. **Click Refresh Contacts** <br /><br />


3. Number registered:

![ContactsScreen](/resources/images/SyncContacts3.jpg) <br /><br /> <br /><br />


### Deregister Number

1. Deregister number:

![Deregister](/resources/images/SyncContacts4.jpg) <br /><br />


2. **Click Refresh Contacts** <br /><br />


3. Number deregistered:

![ContactsScreen](/resources/images/SyncContacts5.jpg) <br /><br />
