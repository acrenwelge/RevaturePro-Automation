# RevaturePro Automation
Automating tasks on RevaturePro

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

You also need a "revpro-exam-urls.txt" file within an "imocha-uploads" directory in the "Documents" folder. 

## Running the script
```bash
java -jar app.jar search # searches for exam URLs
java -jar app.jar scrape # reads exam URLs from file and extracts to Excel format
```
