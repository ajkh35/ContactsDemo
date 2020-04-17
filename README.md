# ContactsDemo V2

Hi guys. This is the version 2 of the ContactsDemo app. I have made some improvements after everyone's experience with the previous app. I will highlight some of those changes here.

## Kotlin

I have switched the programming language to Kotlin for this app. I hope this will make the code more concise and easy to read.

## Demo Server

So this app works in tandem with a very simple node server. The server doesn't do much but maintains a list of numbers. This list is synced with the device's numbers via the SyncAdapter. You will need to have node version 12.16.2 and npm version 6.14.4 to run the server.

## Setup

Use command node to start the server on localhost:3000

```bash
cd demo_server
node app.js
```
Then launch the app

First screen:
![MainScreen](/resources/images/SyncContacts1.jpg)

### Register Number

Register number:

![Register](/resources/images/SyncContacts2.jpg)

Click Refresh Contacts

Number registered:

![ContactsScreen](/resources/images/SyncContacts3.jpg)

### Deregister Number

Deregister number:

![Deregister](/resources/images/SyncContacts4.jpg)

Click Refresh Contacts

Number deregistered:

![ContactsScreen](/resources/images/SyncContacts5.jpg)
