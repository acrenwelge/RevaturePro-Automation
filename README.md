# RevaturePro Automation
Automating tasks on RevaturePro

## Features
- Automate searching and recording of exam URLs
- Extract RevPro quizzes into Excel files formatted for imocha upload given file with exam URLs

## Getting Started
```bash
git clone https://github.com/acrenwelge/RevaturePro-Automation/
cd RevaturePro-Automation/
mvn package
```

### Environment Variables
You must set the following environment variables:
- CHROMEDRIVER: path to the executable for Chrome
- REVPRO_USERNAME: login username for RevaturePro
- REVPRO_PW: password for RevaturePro login

You also need to create a file:
- at the filepath: "{user.home}/Documents/imocha-uploads/revpro-exam-urls.txt"
- it should contain the list of RevPro exam URLs for the program to extract, separated by line
or you can use the 'search' feature of the app to do this for you

## Running the script
```bash
java -jar app.jar search # searches for exam URLs
# outputs the URLs to 'revpro-exam-urls.txt' as described above
java -jar app.jar scrape # reads exam URLs from file and extracts to Excel format
# writes to 'extracted-revpro-exams' directory
```

### TODO
- automatically create the imocha-uploads folder and revpro-exam-urls file if they don't exist
- when searching, allow user to pass a string search query
- multithreading
