# OBJ

This application was written as a final exam for Object-Oriented Programming 2 at the University of South-eastern Norway. The application was developed for software application testers to submit cases like UI-errors, backend-errors i.e for developers to handle. A leader role was created to assign cases to developers, as well as setting status for cases (Submitted, assigned, solved etc.). The application is far from finished, as we had 3 days to make this from scratch during the exam, which was a homebased exam where AI was not allowed to use. The application was built to the best of our abilities as 4th semester students.

The application is a combination of one client program, and one multiple-users server program. The application is written in Java for both backend and frontend, with JavaFX for the user interface.

If you want to run this application, you will need to make a .txt file and call it db.properties containing the following:

db.url=jdbc:mysql://localhost:3306/sakssystem?serverTimezone=UTC
db.user={YOUR MYSQL USERNAME}
db.password={YOUR MYSQL PASSWORD}

After you have done this, run the FlerBrukerServer.java file before running App.java. If the database does not exist, it will be created the first time you run FlerBrukerServer.java. Console prints will tell you if this was successfull.

Screenshots of the running application will follow:

![Login screen](/sakshandteringssystem/src/main/java/com/eksamen2025/ressurser/screenshots/image1.png)

![Tester view](/sakshandteringssystem/src/main/java/com/eksamen2025/ressurser/screenshots/image3.png)

![Tester view 2](/sakshandteringssystem/src/main/java/com/eksamen2025/ressurser/screenshots/image4.png)