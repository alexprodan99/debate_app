import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Ideone
{
    public static void main (String[] args) throws java.lang.Exception
    {
        Scanner scanner = new Scanner(System.in);
        String userInput = scanner.nextLine();
        String[] splits = userInput.split(":");
        String[] topics = splits[0].split("\\+");
        Pattern messagePattern = Pattern.compile("(\"[^\"]*\"|'[^']*')");
        Matcher matcher = messagePattern.matcher(splits[1]);

        List<String> messages = new ArrayList<String>();


        String res=null;
        int index = 0;
        while(matcher.find(index) && (res=matcher.group())!=null)
        {
            messages.add(res);
            index=matcher.end();
        }


        if(topics.length != messages.size()) {
            System.out.println("Number of topics is not equal to number of messages!");
        } else {
            int size = messages.size();
            for(int i = 0; i < size; i++) {
                String message = messages.get(i);
                String topic = topics[i];

                System.out.println(message + " " + topic);
            }
        }

//        if(matcher.matches() && count != 0) {
//            if(count != topics.length) {
//                System.out.println("Number of topics is not equal to number of messages!");
//            }else{
//                for(int i = 0; i < count; i++) {
//                    String message = matcher.group(i+1);
//                    String topic = topics[i];
//                    System.out.println("topic=" + topic + " " + "message=" + message);
//                }
//            }
//        }
    }
}