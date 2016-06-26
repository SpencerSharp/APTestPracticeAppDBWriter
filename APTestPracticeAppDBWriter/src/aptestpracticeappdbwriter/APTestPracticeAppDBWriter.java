package aptestpracticeappdbwriter;

import static aptestpracticeappdbwriter.DynamoHandler.cutDash;
import static java.lang.System.out;
import java.io.*;
import java.util.*;

/**
 *
 * @author spencersharp
 */
public class APTestPracticeAppDBWriter
{
    public static void main(String[] args) throws IOException
    {
        DynamoHandler db = new DynamoHandler();
        db.incrementVersion();
        //First thing is increment version
        Scanner sc = new Scanner(new File("/Users/spencersharp/Desktop/APTestPracticeAppInput.txt"));
        
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        ArrayList<Topic> topics = new ArrayList<Topic>();
        ArrayList<Question> questions = new ArrayList<Question>();
        ArrayList<AnswerChoice> answerChoices = new ArrayList<AnswerChoice>();
        long subjectID = 1;
        long topicID = 1;
        long questionID = 1;
        long answerChoiceID = 1;
        while(sc.hasNext())
        {
            String subjectNameLine = sc.nextLine();
            String subjectName = subjectNameLine.substring(9);
            String subjectTopicIDs = "";
            String curLine = sc.nextLine();
            while(!curLine.startsWith("Subject:"))
            {
                String topicNameLine = curLine;
                String topicName = topicNameLine.substring(7);
                String topicQuestionIDs = "";
                curLine = sc.nextLine();
                while(!curLine.startsWith("Topic:") && !curLine.startsWith("Subject:"))
                {
                    String questionTextLine = curLine;
                    String questionText = questionTextLine.substring(2);
                    String questionAnswerChoiceIDs = "";
                    for(int i = 0; i < 5; i++)
                    {
                        curLine = sc.nextLine();
                        //out.println(curLine);
                        String answerChoiceLine = curLine;
                        int ansChoice = answerChoiceLine.charAt(0)-64;
                        String answerChoiceText = answerChoiceLine.substring(2);
                        //Add answerChoiceID to questionAnswerChoiceIDs
                        questionAnswerChoiceIDs += answerChoiceID + "-";
                        //Make AnswerChoice Object
                        AnswerChoice answerChoice = new AnswerChoice(answerChoiceID++,ansChoice,answerChoiceText);
                        answerChoices.add(answerChoice);
                    }
                    //out.println("out");
                    curLine = sc.nextLine();
                    int correctAnswerChoice = curLine.charAt(0)-64;
                    //Add questionID to topicQuestionIDs
                    topicQuestionIDs += questionID + "-";
                    //Make Question object and add it to the Questions ArrayList
                    Question question = new Question(questionID++,questionText,cutDash(questionAnswerChoiceIDs),correctAnswerChoice);
                    questions.add(question);
                    //Increment the line
                    if(sc.hasNextLine())
                        curLine = sc.nextLine();
                    else
                        curLine = "Subject:";
                }
                //Add topicID to subjectTopicIDs
                subjectTopicIDs += topicID + "-";
                //Make Topic Object and add it to the topics ArrayList
                Topic topic = new Topic(topicID++,topicName,cutDash(topicQuestionIDs));
                topics.add(topic);
            }
            //Make Subject Object and add it to the subjects ArrayList
            Subject subject = new Subject(subjectID++,subjectName,cutDash(subjectTopicIDs));
            subjects.add(subject);
        }
        db.setSubjects(subjects);
        out.println(db.getSubjects());
        db.setTopics(topics);
        out.println(db.getTopics());
        db.setQuestions(questions);
        out.println(db.getQuestions());
        db.setAnswerChoices(answerChoices);
        out.println(db.getVersion());
        
        
        
        out.println(db.getAnswerChoices());

        ArrayList<Student> students = new ArrayList<Student>();
        ArrayList<QuestionData> questionDataList = new ArrayList<QuestionData>();
        sc = new Scanner(new File("/Users/spencersharp/Desktop/APTestPracticeAppUserData.txt"));
        long studentID = 1;
        long questionDataID = 1;
        while(sc.hasNext())
        {
            String curLine = sc.nextLine();
            String username = curLine.substring(9);
            curLine = sc.nextLine();
            String password = curLine.substring(10);
            String s = "";
            for(Question question : questions)
            {
                QuestionData questionData = new QuestionData(questionDataID++,question.getID(),question.getCorrectAnswerChoice(),0);
                s+=questionData.getID()+"-";
                questionDataList.add(questionData);
            }
            s=cutDash(s);
            Student student = new Student(studentID++, username, password,"0",s);
            students.add(student);
        }
        out.println(db.setStudents(students));
        out.println(db.getStudents());
        out.println(db.setQuestionData(questionDataList));
        out.println(db.getQuestionData());
    }
    
    public static String cutDash(String s)
    {
        String ret = s.substring(0,s.length()-1);
        return ret;
    }
}