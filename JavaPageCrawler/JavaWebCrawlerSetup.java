package JavaPageCrawler;
import java.net.URL;
import java.util.*;
import java.nio.*;
class JavaWebCrawlerSetup{
	public static void init(String projectName,String url,boolean forceSubDomain){
		try{
			URL u = new URL(url);
			String domainName = u.getAuthority();
			JavaWebCrawler.makeDirs(projectName+"\\res");
			JavaWebCrawler.makeDirs(projectName+"\\temp");
			JavaWebCrawler.writeFile(projectName + "\\temp\\queue.txt",url);
			JavaWebCrawler.writeFile(projectName + "\\temp\\crawled.txt","");
			Properties prop  = new Properties();
			prop.setProperty("PROJECT_NAME",projectName);
			prop.setProperty("BASEURL",url);
			prop.setProperty("DOMAIN_NAME",domainName);
			if(forceSubDomain){
				prop.setProperty("FORCE_SUB_DOMAIN","TRUE");
			}else{
				prop.setProperty("FORCE_SUB_DOMAIN","FALSE");
			}
			JavaWebCrawler.setProperties(prop,projectName+"\\res\\project.properties");
		}catch(Exception e){
			JavaWebCrawler.errorHandle("Setup | " + e.getMessage(),true);
		}
	}
	public static void refreshProgress(){
		try{
			JavaWebCrawler.copyFile("progress\\queue.txt","temp\\queue.txt");
			JavaWebCrawler.copyFile("progress\\crawled.txt","temp\\crawled.txt");
		}catch(Exception e){
			JavaWebCrawler.errorHandle("Refresh progress | " + e.getMessage());
		}
	}
}