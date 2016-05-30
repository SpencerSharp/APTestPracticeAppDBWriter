package aptestpracticeappdbwriter;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by spencersharp on 5/16/16.
 */
public class DynamoHandler
{
    DynamoDBMapper mapper;
    public DynamoHandler() throws IOException
    {
        
        Scanner sc = new Scanner(new File("credentials.txt"));
        String accessKey = sc.next();
        String secretKey = sc.next();
        //AWSCredentials credentials = new BasicAWSCredentials(""+R.string.db_accessKey,""+R.string.db_accessKey);
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        AmazonDynamoDBClient client = new AmazonDynamoDBClient(credentials).withEndpoint("dynamodb.us-west-2.amazonaws.com");
        mapper = new DynamoDBMapper(client);
    }

    public void incrementVersion()
    {
        Version version = new Version();
        try{
            version = mapper.load(Version.class,0);
            version.increment();
        }catch(Exception e)
        {
            version = new Version(0);
        }
        mapper.save(version);
    }

    public long getVersion()
    {
        Version version = mapper.load(Version.class,0);

        return version.getVersion();
    }





    //Login info method
    public boolean tryRegister(String username, String password)
    {
        ArrayList<Student> students = getStudents();
        boolean nameTaken = false;
        long maxID = 0;
        for(Student student : students)
        {
            if(student.getID()>maxID)
                maxID = student.getID();
            if(student.equals(username))
            {
                nameTaken = true;
            }
        }
        if(!nameTaken)
        {
            Student student = new Student(maxID,username,password,"",generateQuestionData());
            setStudent(student);
            return true;
        }
        else
            return false;
    }

    public long tryLogin(String username, String password)
    {
        for(Student student : getStudents())
        {
            if(student.getUsername().equals(username) && student.getPassword().equals(password))
                return student.getID();
        }
        return -1;
    }

    public String generateQuestionData()
    {
        String s = "";
        long maxQuestionDataID = 0;
        for(QuestionData questionData : getQuestionData())
        {
            if(questionData.getID() > maxQuestionDataID)
                maxQuestionDataID = questionData.getID();
        }
        long curID = maxQuestionDataID+1;
        for(Question question : getQuestions())
        {
            QuestionData questionData = new QuestionData(curID++,question.getID(),question.getCorrectAnswerChoice(),0);
            s+=questionData.getID()+"-";
            setQuestionData(questionData);
        }
        s=cutDash(s);
        return s;
    }

    public static String cutDash(String s)
    {
        String ret = s.substring(0,s.length()-1);
        return ret;
    }





    //Subject methods
    public Subject getSubjectFromID(long id)
    {
        Subject subject = mapper.load(Subject.class,id);
        return subject;
    }

    public Subject setSubject(Subject subject)
    {
        mapper.save(subject);
        return subject;
    }

    public Subject deleteSubject(Subject subject)
    {
        mapper.delete(subject);
        return subject;
    }

    public ArrayList<Subject> getSubjects()
    {
        ArrayList<Subject> subjects = new ArrayList<Subject>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Subject> scanSubjects = mapper.scan(Subject.class,scanExpression);
        for(Subject subject : scanSubjects)
        {
            subjects.add(subject.clone());
        }

        return (ArrayList)subjects;
    }

    public ArrayList<Subject> setSubjects(ArrayList<Subject> subjects)
    {
        List<Subject> curSubjects = getSubjects();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(Subject subject : curSubjects)
        {
            boolean hasFoundSameID = false;
            for(Subject subject2 : subjects)
            {
                if(subject.getID()==subject2.getID())
                {
                    idsSaved.add(subject2.getID());
                    setSubject(subject2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteSubject(subject);
            }
        }

        for(Subject subject : subjects)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==subject.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setSubject(subject);
            }
        }
        return subjects;
    }





    //Topic Methods
    public Topic getTopicFromID(long id)
    {
        Topic topic = mapper.load(Topic.class,id);
        return topic;
    }

    public Topic setTopic(Topic topic)
    {
        mapper.save(topic);
        return topic;
    }

    public Topic deleteTopic(Topic topic)
    {
        mapper.delete(topic);
        return topic;
    }

    public ArrayList<Topic> getTopics()
    {
        ArrayList<Topic> topics = new ArrayList<Topic>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Topic> scanTopics = mapper.scan(Topic.class,scanExpression);
        for(Topic topic : scanTopics)
        {
            topics.add(topic.clone());
        }
        return (ArrayList)topics;
    }

    public ArrayList<Topic> setTopics(ArrayList<Topic> topics)
    {
        List<Topic> curTopics = getTopics();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(Topic topic : curTopics)
        {
            boolean hasFoundSameID = false;
            for(Topic topic2 : topics)
            {
                if(topic.getID()==topic2.getID())
                {
                    idsSaved.add(topic2.getID());
                    setTopic(topic2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteTopic(topic);
            }
        }

        for(Topic topic : topics)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==topic.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setTopic(topic);
            }
        }
        return topics;
    }





    //Question methods
    public Question getQuestionFromID(long id)
    {
        Question question = mapper.load(Question.class,id);
        return question;
    }

    public Question setQuestion(Question question)
    {
        mapper.save(question);
        return question;
    }

    public Question deleteQuestion(Question question)
    {
        mapper.delete(question);
        return question;
    }

    public ArrayList<Question> getQuestions()
    {
        ArrayList<Question> questions = new ArrayList<Question>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Question> scanQuestions = mapper.scan(Question.class,scanExpression);
        for(Question question : scanQuestions)
        {
            questions.add(question.clone());
        }

        return (ArrayList)questions;
    }

    public ArrayList<Question> setQuestions(ArrayList<Question> questions)
    {
        List<Question> curQuestions = getQuestions();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(Question question : curQuestions)
        {
            boolean hasFoundSameID = false;
            for(Question question2 : questions)
            {
                if(question.getID()==question2.getID())
                {
                    idsSaved.add(question2.getID());
                    setQuestion(question2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteQuestion(question);
            }
        }

        for(Question question : questions)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==question.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setQuestion(question);
            }
        }
        return questions;
    }





    //AnswerChoice Methods
    public AnswerChoice getAnswerChoiceFromID(long id)
    {
        AnswerChoice answerChoice = mapper.load(AnswerChoice.class,id);
        return answerChoice;
    }

    public AnswerChoice setAnswerChoice(AnswerChoice answerChoice)
    {
        mapper.save(answerChoice);
        return answerChoice;
    }

    public AnswerChoice deleteAnswerChoice(AnswerChoice answerChoice)
    {
        mapper.delete(answerChoice);
        return answerChoice;
    }

    public ArrayList<AnswerChoice> getAnswerChoices()
    {
        ArrayList<AnswerChoice> answerChoices = new ArrayList<AnswerChoice>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<AnswerChoice> scanAnswerChoices = mapper.scan(AnswerChoice.class,scanExpression);
        for(AnswerChoice answerChoice : scanAnswerChoices)
        {
            answerChoices.add(answerChoice.clone());
        }

        return (ArrayList)answerChoices;
    }

    public ArrayList<AnswerChoice> setAnswerChoices(ArrayList<AnswerChoice> answerChoices)
    {
        List<AnswerChoice> curAnswerChoices = getAnswerChoices();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(AnswerChoice answerChoice : curAnswerChoices)
        {
            boolean hasFoundSameID = false;
            for(AnswerChoice answerChoice2 : answerChoices)
            {
                if(answerChoice.getID()==answerChoice2.getID())
                {
                    idsSaved.add(answerChoice2.getID());
                    setAnswerChoice(answerChoice2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteAnswerChoice(answerChoice);
            }
        }

        for(AnswerChoice answerChoice : answerChoices)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==answerChoice.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setAnswerChoice(answerChoice);
            }
        }
        return answerChoices;
    }





    //Student methods
    public Student getStudentFromID(long id)
    {
        Student student = mapper.load(Student.class,id);
        return student;
    }

    public Student setStudent(Student student)
    {
        mapper.save(student);
        return student;
    }

    public Student deleteStudent(Student student)
    {
        mapper.delete(student);
        return student;
    }

    public ArrayList<Student> getStudents()
    {
        ArrayList<Student> students = new ArrayList<Student>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<Student> scanStudents = mapper.scan(Student.class,scanExpression);
        for(Student student : scanStudents)
        {
            students.add(student.clone());
        }

        return (ArrayList)students;
    }

    public ArrayList<Student> setStudents(ArrayList<Student> students)
    {
        List<Student> curStudents = getStudents();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(Student student : curStudents)
        {
            boolean hasFoundSameID = false;
            for(Student student2 : students)
            {
                if(student.getID()==student2.getID())
                {
                    idsSaved.add(student2.getID());
                    setStudent(student2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteStudent(student);
            }
        }

        for(Student student : students)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==student.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setStudent(student);
            }
        }
        return students;
    }





    //QuestionData methods
    public QuestionData getQuestionDataFromID(long id)
    {
        QuestionData questionData = mapper.load(QuestionData.class,id);
        return questionData;
    }

    public QuestionData setQuestionData(QuestionData questionData)
    {
        mapper.save(questionData);
        return questionData;
    }

    public QuestionData deleteQuestionData(QuestionData questionData)
    {
        mapper.delete(questionData);
        return questionData;
    }

    public ArrayList<QuestionData> getQuestionData()
    {
        ArrayList<QuestionData> questionDataList = new ArrayList<QuestionData>();
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression();
        PaginatedScanList<QuestionData> scanQuestionData = mapper.scan(QuestionData.class,scanExpression);
        for(QuestionData questionData : scanQuestionData)
        {
            questionDataList.add(questionData.clone());
        }

        return (ArrayList)questionDataList;
    }

    public ArrayList<QuestionData> setQuestionData(ArrayList<QuestionData> questionDataList)
    {
        List<QuestionData> curQuestionData = getQuestionData();
        ArrayList<Long> idsSaved = new ArrayList<Long>();
        for(QuestionData questionData : curQuestionData)
        {
            boolean hasFoundSameID = false;
            for(QuestionData questionData2 : questionDataList)
            {
                if(questionData.getID()==questionData2.getID())
                {
                    idsSaved.add(questionData2.getID());
                    setQuestionData(questionData2);
                    hasFoundSameID = true;
                    break;
                }
            }
            if(!hasFoundSameID)
            {
                deleteQuestionData(questionData);
            }
        }

        for(QuestionData questionData : questionDataList)
        {
            boolean hasIDbeenSaved = false;
            for(Long id : idsSaved)
            {
                if(id==questionData.getID())
                {
                    hasIDbeenSaved = true;
                    break;
                }
            }
            if(!hasIDbeenSaved)
            {
                setQuestionData(questionData);
            }
        }
        return questionDataList;
    }
}