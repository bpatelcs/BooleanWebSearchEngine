##Title Tag Based Boolean Web Search Engine
This program parses the web pages reachable from the seed web page given as input and index their title tag content and use this information to give the result for particular query.Boolean OR search is used to give the the set of URLs as a result.I wrote this program to understand how search engines like Google, Bing work.You can modify to index the whole content of webpage instead of just title tag content.Search is completely different from what commercial search engines use.They generally use probabilistic models,user feedback and lots of other stuffs to give good results.


##How To Run This Program

There are two files,

#####`WebCrawler.java`: This file contains the code for crawler.There are two parameters to create new instance of web crawler.

######1.Seed: Basically it is some famous webpage which contains lots of links to some famous webpages.

######2.Limit: As we run our program on desktop, there will be always limit up to which you can index the content of title tag because of limited RAM.So, limit parameter makes sure that program does not visit more webpages than limit.


#####`Client.java`:This is the file which takes queries and returns the set of URLs as a result.

Example:

![alt text](https://www.dropbox.com/s/jvprvff2464jzbo/PDFDownloaderScreenShot.JPG "Logo Title Text 1")

