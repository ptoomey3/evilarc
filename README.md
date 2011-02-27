# evilarc

## Purpose
evilarc lets you create a zip file that contains files with directory traversal characters in their embedded path.  Most commercial zip program (winzip, etc) will prevent extraction of zip files whose embedded files contain paths with directory traversal characters.  However, many software development libraries do not include these same protection mechanisms (ex. Java, PHP, etc). 
