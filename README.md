Friends List
============
Thanks for installing my android application.
I made this application as an assignment for my android class.
You are free to use anything and alter anything.

## Requirements
**Webserver**
 * PHP Version 7.3.3  
 * Database 10.1.38-MariaDB  
(these versions is what I used)

**Android**
 * Minimum API Level: 23
 * Run / Build API Level: 27
 * Emulator: Nexus 5x, API 27


## Installation
Because the app uses an API to store and load data, you need to install your own webserver.
You can find the webserver PHP and database SQL file in the webserver folder of the project.
  1. Install the MYSQL database using the database.sql inside the webserver folder.
  2. Drag the index.php file to the root of your webserver.
  3. Change the database credentials inside the index.php file.
  4. Change API_URL inside app/src/main/java/nl/bonfire17/friendslist/NetworkSingleton.java.
