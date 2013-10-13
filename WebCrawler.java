

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler implements Runnable {

    private static Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
    private static Hashtable<String, ArrayList<URL>> indexes = new Hashtable<String, ArrayList<URL>>();
    private static URL seed;
    private static int limit;
    private static int index = 0;

    public void run() {
        try {
            startCrawling();
        } catch (MalformedURLException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WebCrawler(String s, int n) throws MalformedURLException, IOException {
        seed = new URL(s);
        limit = n;
    }

    private void startCrawling() throws MalformedURLException, IOException {

        hash.put(seed.toString(), index++);

        LinkedList<URL> URLs = new LinkedList<URL>();
        URLs.addLast(seed);
       
        Pattern p = Pattern.compile("(?<=<a href=\\\")(http:\\/\\/[w]+\\.[\\w\\d]+\\.(com|gov|us|edu|in))");
        Pattern q = Pattern.compile("(?<=<title>)[\\w\\d\\S\\s]+(?=<\\/title>)");
        int total_indexing = 0;
        while (!URLs.isEmpty() && total_indexing++ < limit) {
            URL url = URLs.removeFirst();
            String HTML = read(url);
            if (HTML == null) {
                continue;
            }
            Matcher m = p.matcher(HTML);
            addURLsToLinkedList(m, URLs, index - 1);
            Matcher n = q.matcher(HTML);
            doIndexing(url, n);
        }

    }

    public static Iterable<URL> query(String query) {
        String s = query.toLowerCase();
        String[] split = s.split(" ");        
        Set<URL> result=new HashSet<URL>();
        for (String word : split) {

            copy(indexes.get(word), result);
        }
        if (result.isEmpty()) {
            return null;
        }
        return result;
    }

    private static void copy(ArrayList<URL> urls, Set<URL> result) {
        int i = 0;
        if (urls == null) {
            return;
        }
        
        while (i < urls.size()) {
             URL url=urls.get(i);                           
                result.add(url);                                          
                i++;
        }
    }

    private static void doIndexing(URL url, Matcher n) throws MalformedURLException {
        String regex = "[\\W\\d\\s]";
        //String regex = "[/|/,/-/'\\d\\s/.]";
        HashMap<String, Integer> wordHash = new HashMap<String, Integer>();
        String replace = "";
        if (n.find() == false) {
            return;
        }
        String title = n.group();
        title = title.toLowerCase();
        String[] words = title.split(" ");
        //System.out.print(url + " ");
        for (String s : words) {
            s = s.replaceAll(regex, replace);
            if (wordHash.containsKey(s)) {
                continue;
            }
            wordHash.put(s, 1);
            //System.out.print(s + " ");
            if (indexes.get(s) != null) {
                indexes.get(s).add(url);
            } else {
                ArrayList<URL> temp = new ArrayList<URL>();
                temp.add(url);
                indexes.put(s, temp);
            }
        }        
    }

    private static void addURLsToLinkedList(Matcher m, LinkedList<URL> URLs, int j) {

        while (m.find()) {
            String s = m.group().trim();

            if (hash.get(s) != null) {
                continue;
            }
            hash.put(s, index++);            
            try {
                URLs.addLast(new URL(s));
            } catch (MalformedURLException ex) {
                return;
            }
        }
    }

    public static String read(URL url) throws MalformedURLException, IOException {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(url.openStream()));
        } catch (Exception e) {
            return null;
        }
        String HTML = null;
        String line;
        while ((line = br.readLine()) != null) {
            HTML += line + "\n";
        }
        br.close();
        return HTML;
    }
}