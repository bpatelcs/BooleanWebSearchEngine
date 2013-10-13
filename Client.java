import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Client {
    
    public static void main(String args[]) throws MalformedURLException, IOException {
       WebCrawler one = new WebCrawler("http://www.cnn.com", 1000);
      
       Thread crawler=new Thread(one);
       crawler.start();
        Scanner s = new Scanner(System.in);
        System.out.print("Enter Query:");
        while (s.hasNext()) {            
            String query = s.nextLine().trim();
            //System.out.println(query);
            Iterable<URL> result=one.query(query);
            if(result==null) {
                System.out.println("No Result Found!!");
                System.out.print("Enter Query:");
                continue;
            }            
            System.out.println(result);
            System.out.print("Enter Query:");
        }
    }
    
    
    
}
