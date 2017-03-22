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
	
	public JavaWebCrawlerSpider(String p,String b, String d){
		projectName = p;
		baseUrl = b;
		domainName = d;
		queueFilePath = projectName + "/queue.txt";
		crawledFilePath = projectName + "/crawled.txt";
		boot();
		crawlPage("First spider", this.baseUrl);
	}
	public static void main(String[] args){
		
	//	Document doc = Jsoup.connect(url).get();
	}
	static void boot(){
		JavaWebCrawler.startFromSpider(projectName,baseUrl);
		queue = JavaWebCrawler.fileToSet(queueFilePath);
	}
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
	static Set<String> gatherLinks(String pageUrl){
		try{
			Document doc = Jsoup.connect(pageUrl).get();
			return JavaWebCrawlerLinkFind.parsePage(doc);
		}catch(Exception e){
			JavaWebCrawler.errorHandle("ERROR: Spider | " + e.getMessage() );
			return null;
		}
	}
	static void addLinksToQueue(Set<String> links){
		if(links!=null){
			for(String link : links){
				if(queue.contains(link)){
					continue;
				}else if(crawled.contains(link)){
					continue;
				}else if(domainName.indexOf(JavaWebCrawlerDomain.getDomainName(link))==-1){
					continue;
				}
				queue.add(link);
			}
		}
	}
	static void updateFiles(){
		JavaWebCrawler.setToFile(queue,queueFilePath);
		JavaWebCrawler.setToFile(crawled,crawledFilePath);
		JavaWebCrawlerRun.crawl();
	}
}