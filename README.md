# reading_bubby
Cs-335 Software Engineering Project

# To Run in Visual Studio Code:

- Clone the reading_bubby github repository or download the zip file of the repository and unzip it (all the subdirectories and files should already be inside in a directory called “reading_bubby”)
- Create a new directory (it can have any name, we use Reading_Buddy for the sake of clarity) and place the **reading_bubby** directory inside the newly created directory
- Open the containing directory in a new window in Visual Studio Code
- In the **Explorer** side panel navigate reading_bubby > src > ReadingBuddy.java (click on the file to open it).
  - This will turn a few of the file and directory names red, you will see a red error line where opencsv is imported, etc.
- At the bottom of the Explorer panel click on **Java Projects** and scroll to the bottom of that section
- At the bottom of the Java Projects section, hover over **Referenced Libraries** and click the plus sign (**+**). This will bring up a file selection window.
- In the file selector go into the reading_bubby directory, and then into the **lib** directory, _select **all** the files_ in the lib directory, and click **Select Jar Libraries**.
- The program can then be run!


# File Structure

Reading_Buddy

    |––reading_bubby
         |–– appdata   
         |    |–– book_groups.csv
         |    |–– books.csv
         |    |–– currently_reading.csv
         |    |–– group_posts.csv
         |    |–– next_id_nums.csv
         |    |–– read_books.csv
         |    |–– search_posts.csv
         |    |–– users.csv
         |
         |–– lib
         |    |–– commons-lang3-3.14.0-javadoc.jar
         |    |–– commons-lang3-3.14.0-sources.jar
         |    |–– commons-lang3-3.14.0-test-sources.jar
         |    |–– commons-lang3-3.14.0-tests.jar
         |    |–– commons-lang3-3.14.0.jar
         |    |–– opencsv-5.9.jar
         |
         |–– README.md
         |
         |–– src
         |    |–– BookGroup.java
         |    |–– Book.java
         |    |–– CurrentBook.java
         |    |–– GroupPost.java
         |    |–– ReadBook.java
         |    |–– ReadingBuddy.java
         |    |–– SearchPost.java
         |    |–– User.java
