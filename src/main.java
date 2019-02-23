import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class main {
    public static void main(String[] args) throws IOException {
        urlGetter url = new urlGetter();
        url.urlgetter();
        url.contentGetter();

    }
}

class urlGetter {

    public ArrayList urlList = new ArrayList(); //array list containing all the links in the given url, in this case url of different chapters of the book
    public String URl = "http://ds.eywedu.com/jinyong/tlbb/";
    ArrayList titles = new ArrayList();


    void urlgetter(){
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        int startPoint = 0 , finalPoint = 0;
        try {
            url = new URL(URl);
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                line = line.toLowerCase();
                if (line.contains("href")) {
                    String str = line;
                    startPoint = str.indexOf("href") + 6;
                    finalPoint = str.indexOf("\"", startPoint);
                    if (finalPoint != -1) {
                        String newStr = (str.substring(startPoint, finalPoint));
                        if (!newStr.contains("http")) {
                            newStr = URl + newStr;
                            urlList.add(newStr);
                        }
                    }
                }
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                //exception
            }
        }
        urlList.remove(urlList.size()-1);
    }

    void contentGetter() throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter("content.txt"));

        for (int i = 0; i<urlList.size(); i++){
            ArrayList content = new ArrayList();
            URL url;
            InputStream is = null;
            BufferedReader br;
            String line;
            try {
                url = new URL((String)urlList.get(i));
                is = url.openStream();
                br = new BufferedReader(new InputStreamReader(is));

                while ((line = br.readLine()) != null) {
                    content.add(line);
                }

                boolean title = false;
                ArrayList filtered = new ArrayList();
                for (int m = 0; m<content.size(); m++){
                    String contents = (String) content.get(m);
                    if (contents.contains("script type=\"text") && !title){
                        title = true;
                        if (contents.indexOf("</font>") <0){
                            writer.write(contents.substring(contents.indexOf(">", contents.indexOf("font"))+1, contents.indexOf("&",contents.indexOf("font"))));
                            writer.newLine();
                        }else{
                            writer.write(
                                    contents.substring(contents.indexOf("\"4\"")+4,contents.indexOf("</font>")));
                            writer.newLine();
                            for (int j = 0; j<=+1; j++){
                                content.remove(0);
                            }}
                    }else if (contents.contains("script type=\"text")){
                        int loc = contents.indexOf("</DIV>");
                        int fLoc = contents.indexOf("<BR>");
                        if (loc != -1 && fLoc != -1)
                            writer.write(contents.substring(loc+6, fLoc));
                        writer.newLine();
                    } else if (contents.contains("<BR>")){

                        writer.write(contents.substring(0,contents.indexOf("<BR>")));
                        writer.newLine();
                    }


                }

            } catch (MalformedURLException mue) {
                mue.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                try {
                    if (is != null) is.close();
                } catch (Exception e) {
                    System.out.println(e);
                }
            }



        }
        writer.close();
    }


}
