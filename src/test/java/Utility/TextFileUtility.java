package Utility;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextFileUtility {


    public static String[] getValueInTextFile(String filepath,int linenumber) throws Exception
    {
        //Access existing text file or CSV file, which consists of test data
        File f=new File(filepath);
        FileReader fr=new FileReader(f);
        BufferedReader br=new BufferedReader(fr);
        //move up to a line which is the before line for required in sequential text file
        for(int i=1;i<linenumber;i++)
        {
            br.readLine();
        }
        //Read required line
        String temp=br.readLine();
        String output[]=temp.split(",");
        br.close();
        fr.close();
        return(output);
    }

    public int countOfLines(String Filepath) throws IOException {
        File f = new File(Filepath);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        int count = 0;
        String line = "";
        while ((line = br.readLine()) != null) {
            count++;

        }
        return count;
    }
    public  void CountsOfWordsInEachLine(String Filepath) throws IOException {
        File f = new File(Filepath);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        int ln=1;
        while ((line = br.readLine()) != null) {
            String words[] = line.split(" ");
            if(ln==1)
            {
                System.out.println("1st line has "+words.length+" words");

            }
            else {
                System.out.println(ln+"line has  "+words.length+"words");
            }
            ln++;

        }
        br.close();
        fr.close();

    }
    public  void  displayNumbersInEachLine(String Filepath) throws IOException {
        File f = new File(Filepath);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        Pattern p=Pattern.compile("[0-9]");
        while ((line = br.readLine()) != null) {
            Matcher m=p.matcher(line);
            while(m.find())
            {
                System.out.println(m.group());
            }
        }
        br.close();
        fr.close();

    }
    public void DisplayText(String Filepath) throws IOException {
        File f = new File(Filepath);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
    }
    public  void  displayUppercaseWordsInEachLine(String Filepath) throws IOException {
        File f = new File(Filepath);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String line = null;
        Pattern p=Pattern.compile("[A-Z][A-Z]+");
        while ((line = br.readLine()) != null) {
            Matcher m=p.matcher(line);
            while(m.find())
            {
                System.out.println(m.group());
            }
        }
        br.close();
        fr.close();

    }
    public  void findingDuplicatelines(String Filepath) throws IOException {
        //Open existing file in read mode
        File f=new File(Filepath);
        FileReader fr=new FileReader(f);
        BufferedReader br=new BufferedReader(fr);
        List<String> lines=new ArrayList<String>();
        String line="";
        while((line=br.readLine())!=null)
        {
            int flag=0;
            //compare new line with existing lines to skip duplicates
            for(int i=0;i<lines.size();i++)
            {
                if(line.equalsIgnoreCase(lines.get(i)))
                {
                    flag=1;
                    break;
                }
            }
            if(flag==0)
            {
                lines.add(line);
            }
        }
        //close file in Read mode
        br.close();
        fr.close();
        //Open same file in write mode(Existing data will be overwritten)
        FileWriter fw=new FileWriter(f);
        BufferedWriter bw=new BufferedWriter(fw);
        for(int i=0;i<lines.size();i++)
        {
            bw.write(lines.get(i));
            bw.newLine();
        }
        //close file in write mode
        bw.close();
        fw.close();

    }

    public  void lowercaseToUppercase(String Filepath) throws IOException {
        //Open existing file in read mode
        File f=new File(Filepath);
        FileReader fr=new FileReader(f);
        //Load file into RAM as text file
        BufferedReader br=new BufferedReader(fr);
        //Get words starts with any case in each line
        Pattern p=Pattern.compile("[A-Za-z][a-z]+");
        List<String> ls=new ArrayList<>();
        String line=null;
        while((line=br.readLine())!=null) //loop terminates after last line's reading
        {
            Matcher m=p.matcher(line);
            while(m.find())
            {
                System.out.println(m.group());
                ls.add(m.group());
            }
        }
        //convert init lower to init upper
        for(String l:ls)
        {
            char fl=l.charAt(0); //get first letter
            l=l.substring(1); //Take from 2nd to remove first letter
            if(fl>=97) //if lower
            {
                fl=(char) (fl-32); //change first letter to upper case
            }
            l=fl+l; //add converted first letter to remaining word
            System.out.println(l);
        }
        //close file
        br.close();
        fr.close();
    }
}







