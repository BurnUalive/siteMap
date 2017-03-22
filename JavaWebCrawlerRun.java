import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class JavaWebCrawlerRun{
	static String PROJECT_NAME="";
	static String HOMEPAGE ="";
	static String DOMAIN_NAME = "";
	static String QUEUE_FILE = PROJECT_NAME + "/queue.txt";
	static String CRAWLED_FILE = PROJECT_NAME + "/crawled.txt";
	static int NUMBER_OF_THREADS = 2;
	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	public static void main(String[] args){
		int argLength = args.length;
		for(int i=0;i<argLength;i++){
			if(args[i].equals("-p")){
				if(args[i+1].indexOf("-")!=0){
					PROJECT_NAME = args[i+1];
				}else{
					JavaWebCrawler.errorHandle(1,true);
				}
			}else if(args[i].equals("-u")){
				if(args[i+1].indexOf("-")!=0){
					HOMEPAGE = args[i+1];
				}else{
					JavaWebCrawler.errorHandle(1,true);
				}
			}else if(args[i].equals("-d")){
				if(args[i+1].indexOf("-")!=0){
					DOMAIN_NAME = args[i+1];
				}else{
					JavaWebCrawler.errorHandle(1,true);
				}
			}
			QUEUE_FILE = PROJECT_NAME + "/queue.txt";
			CRAWLED_FILE = PROJECT_NAME + "/crawled.txt";
		}
		JavaWebCrawlerSpider jwcs = new JavaWebCrawlerSpider(PROJECT_NAME,HOMEPAGE, DOMAIN_NAME);
		crawl();
	}
	// check for items in queue if so crawl
	public static void crawl(){
		Set<String> queueLinks = JavaWebCrawler.fileToSet(QUEUE_FILE);
		if(queueLinks.size()>0){
			JavaWebCrawler.printLog("Queue link lits size | " + queueLinks.size());
			createJobs();
		}
	}
	// each queue link is a new job
	static void createJobs(){
		try{
			for(String link : JavaWebCrawler.fileToSet(QUEUE_FILE)){
				queue.put(link);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//create worker threads (will die when main exits)
	public static void createSpiders(){
		for(int i=0;i<NUMBER_OF_THREADS;i++){
			JavaWebCrawlerThreads t = new JavaWebCrawlerThreads("Thread " + i );
			t.start();
		}
	}
	
	static class JavaWebCrawlerThreads implements Runnable{
		private Thread t;
		private String threadName;
		JavaWebCrawlerThreads(String name){
			threadName = name;
			JavaWebCrawler.printLog("Creating thread | " + name);
		}
		public void run(){
			JavaWebCrawler.printLog("Running thread | " + threadName);
			
			try{
				while(true){
					String url = queue.poll();
					System.out.println("URL | " + url);
					if(url!=null){
						
						JavaWebCrawlerSpider.crawlPage(threadName,url);
					}else{
						break;
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		public void start(){
			JavaWebCrawler.printLog("Starting thread | " + threadName);
			if(t==null){
				t = new Thread(this,threadName);
				//t.setDaemon(true);
				t.start();
			}
		}
		
	}
	
}