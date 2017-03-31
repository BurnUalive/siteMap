package JavaPageCrawler;
import org.jsoup.*;
import java.util.*;
import java.io.*;
import org.jsoup.select.*;
import org.jsoup.nodes.*;

class JavaWebCrawlerLinkFind{
	// parse a page and get urls
	public static Set<String> parsePage(Document doc){
		try{
			Elements links = doc.select("a[href]");
			Set<String> pageLinks = new HashSet<String>();
			for(Element link : links){
				pageLinks.add(link.attr("abs:href"));
			}
			return pageLinks;
		}catch(Exception err){
			err.printStackTrace();
			return null;
		}
	}
}