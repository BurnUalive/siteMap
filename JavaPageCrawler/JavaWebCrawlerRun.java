package JavaPageCrawler;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

class JavaWebCrawlerRun{
	static String PROJECT_NAME="";
	static String HOMEPAGE ="";
	static String DOMAIN_NAME = "";
	static String QUEUE_FILE = "temp\\queue.txt";
	static String CRAWLED_FILE = "temp\\crawled.txt";
	static int NUMBER_OF_THREADS = 2;
	static boolean FORCE_SUB_DOMAIN = false;
	static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	// run util
	public static void main(String[] args){
		int argLength = args.length;
		boolean flag = false;
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
			}else if(args[i].equals("-r")){
				flag = true;
			}else if(args[i].equals("-f")){
				FORCE_SUB_DOMAIN = true;
			}
		}
		if(argLength==0){
			flag=true;
		}
		if(flag){
			restart();
			startCrawl();
		}else{
			JavaWebCrawlerSetup.init(PROJECT_NAME,HOMEPAGE,FORCE_SUB_DOMAIN);
			JavaWebCrawler.printLog("Project setup complete | " + PROJECT_NAME);
		}
	}
	static void startCrawl(){
		try{
			Runtime r = Runtime.getRuntime();
			Thread t = (Thread)new TaskKill();
			r.addShutdownHook(t);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		JavaWebCrawlerSpider jwcs = new JavaWebCrawlerSpider(PROJECT_NAME,HOMEPAGE, DOMAIN_NAME,FORCE_SUB_DOMAIN);
		crawl();
	}
	// restart handler
	static void restart(){
		try{
		Properties prop = JavaWebCrawler.getProperties("res\\project.properties");
		PROJECT_NAME = prop.getProperty("PROJECT_NAME");
		HOMEPAGE = prop.getProperty("BASEURL");
		DOMAIN_NAME = prop.getProperty("DOMAIN_NAME");
		if(prop.getProperty("FORCE_SUB_DOMAIN").equals("TRUE")){
			FORCE_SUB_DOMAIN = true;
		}else{
			FORCE_SUB_DOMAIN= false;
		}
		JavaWebCrawler.printLog("PROJECT_NAME | " + PROJECT_NAME);
		JavaWebCrawler.printLog("HOMEPAGE | " + HOMEPAGE);
		JavaWebCrawler.printLog("DOMAIN_NAME | " + DOMAIN_NAME);
		JavaWebCrawler.printLog("FORCE_SUB_DOMAIN | " + FORCE_SUB_DOMAIN);
		JavaWebCrawlerSetup.refreshProgress();
		}catch(Exception e){
			JavaWebCrawler.errorHandle("Restart | " + e.getMessage());
		}
		
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
	// Worker thread
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
				t.start();
			}
		}
		
	}
	// On program close threads
	static class TaskKill extends Thread{
		public void run(){
			JavaWebCrawlerSpider.cleanUp();
		}
	}
	
}