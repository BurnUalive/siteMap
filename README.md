# siteMap

## Pre-requisites

Java,JSoup

## Setup
Run compileCrawl.bat
It will create a lib folder in current directory.
Add path\to\lib folder to CLASSPATH environment variable


## Run
'''
java JavaPageCrawler.JavaWebCrawlerRun -p project-name -u url -d domain-name -f
cd project-name
java JavaPageCrawler.JavaWebCrawlerRun -r
'''
-f is flag to force the parser to only parse urls with subdomain = domain name entered

### Project Name Format

Any directory standard string

### Url Format

http(s)://domain-name
e.g https://www.zoho.com

### Domain Name

e.g. zoho.com

TODO: Make it restart from last point
TODO: Fix crawl.bat to run the project-name
TODO: Use system paths to avoid directory not found problems