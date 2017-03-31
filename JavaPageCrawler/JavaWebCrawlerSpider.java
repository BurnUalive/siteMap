package JavaPageCrawler;
import org.jsoup.*;
import java.util.*;
import java.io.*;
import org.jsoup.select.*;
import org.jsoup.nodes.*;
class JavaWebCrawlerSpider{
	// static variable shared amoung all instances
	static String projectName = "";
	static String baseUrl = "";
	static String domainName = "";
	static String queueFilePath = "";
	static String crawledFilePath = "";	
	static Set<String> queue = new HashSet<String>();
	static Set<String> crawled = new HashSet<String>();
	static boolean FORCE_SUB_DOMAIN = false;
	public static boolean ERROR = false;

	// Setup first spider
	public JavaWebCrawlerSpider(String p,String b, String d,boolean fsubdom){
		projectName = p;
		baseUrl = b;
		domainName = d;
		FORCE_SUB_DOMAIN = fsubdom;
		queueFilePath = "temp\\queue.txt";
		crawledFilePath = "temp\\crawled.txt";
		crawlPage("First spider", this.baseUrl);
	}
	// work
	public static void crawlPage(String spiderName,String pageUrl){
		if(queue.contains(pageUrl)||spiderName.equals("First spider")){
			JavaWebCrawler.printLog(spiderName + " | now crawling | " + pageUrl);
			JavaWebCrawler.printLog("Queue length : " + queue.size() + " | Crawled length: "+ crawled.size());
			addLinksToQueue(gatherLinks(pageUrl));
			queue.remove(pageUrl);
			crawled.add(pageUrl);
			updateFiles();
			if(spiderName.equals("First spider")){
				JavaWebCrawlerRun.createSpiders();
			}
		}
	}
	// gather links
	static Set<String> gatherLinks(String pageUrl){
		try{
			Document doc = Jsoup.connect(pageUrl).get();
			return JavaWebCrawlerLinkFind.parsePage(doc);
		}catch(Exception e){
			JavaWebCrawler.errorHandle("Spider | " + e.getMessage() );
			return null;
		}
	}
	// addlinks to queue
	static void addLinksToQueue(Set<String> links){
		if(links!=null){
			for(String link : links){
				if(queue.contains(link)){
					continue;
				}else if(crawled.contains(link)){
					continue;
				}else if(JavaWebCrawlerDomain.getDomainName(link,FORCE_SUB_DOMAIN).indexOf(domainName)==-1){
					continue;
				}
				if(FORCE_SUB_DOMAIN){
					if(domainName.equals(JavaWebCrawlerDomain.getDomainName(link,FORCE_SUB_DOMAIN))||("www."+domainName).equals(JavaWebCrawlerDomain.getDomainName(link,FORCE_SUB_DOMAIN))){
						queue.add(link);
					}
				}else{
					queue.add(link);
				}
			}
		}
	}
	// update files
	static void updateFiles(){
		JavaWebCrawler.setToFile(queue,queueFilePath);
		JavaWebCrawler.setToFile(crawled,crawledFilePath);
		JavaWebCrawlerRun.crawl();
	}
	// clean up on program close
	public static void cleanUp(){
		String progressPath = "progress";
		String queueProgressFilePath = progressPath + "\\queue.txt";
		String crawledProgressFilePath = progressPath + "\\crawled.txt";
		if(queue.size()!=0||ERROR){
			JavaWebCrawler.printLog("Saving Progress.");
			JavaWebCrawler.makeDirs(progressPath);
			JavaWebCrawler.setToFile(queue,queueProgressFilePath);
			JavaWebCrawler.setToFile(crawled,crawledProgressFilePath);
		}else{
			JavaWebCrawler.printLog("Crawling finished");
			JavaWebCrawler.deleteFile(queueProgressFilePath);
			JavaWebCrawler.deleteFile("temp\\queue.txt");
			JavaWebCrawler.deleteFile("temp\\crawled.txt");
			JavaWebCrawler.deleteFile("temp");
			JavaWebCrawler.moveFile(crawledProgressFilePath,"crawled.txt");
			JavaWebCrawler.deleteFile(progressPath);
		}
		JavaWebCrawler.printLog("Clean Up Complete");
	}
}