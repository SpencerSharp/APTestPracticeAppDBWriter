package aptestpracticeappdbwriter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.ArrayList;
import java.util.Arrays;

@DynamoDBTable(tableName = "Students")
public class Student
{
    public long id;
    public String username;
    public String password;
    public String subjectIDs;
    public String questionDataIDs;

    public Student()
    {

    }

    public Student(long id, String username, String password, String subjectIDs, String questionDataIDs)
    {
        this.id = id;
        this.username = username;
        this.password = password;
        this.subjectIDs = subjectIDs;
        this.questionDataIDs = questionDataIDs;
    }

    @DynamoDBHashKey(attributeName = "_id")
    public long getID()
    {
        return id;
    }

    public void setID(long id)
    {
        this.id = id;
    }

    @DynamoDBAttribute(attributeName = "username")
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "password")
    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    @DynamoDBAttribute(attributeName = "subjectIDs")
    public String getSubjectIDsString()
    {
        return subjectIDs;
    }

    public void setSubjectIDsString(String subjectIDsString)
    {
        subjectIDs = subjectIDsString;
    }

    public ArrayList<Long> getSubjectIDsArrayList()
    {
        String[] subjectIDsArray = subjectIDs.split("-");
        ArrayList<Long> subjectIDsArrayList = (ArrayList)Arrays.asList(subjectIDsArray);
        return subjectIDsArrayList;
    }

    /*
    public ArrayList<Subject> getSubjects()
    {
        LocalDBHandler localDB = new LocalDBHandler();

        ArrayList<Long> subjectIDsArrayList = getSubjectIDsArrayList();
        ArrayList<Subject> subjects = new ArrayList<Subject>();

        for(Long subjectID : subjectIDsArrayList)
            subjects.add(localDB.getSubjectFromID(subjectID));

        return subjects;
    }
    */

    @DynamoDBAttribute(attributeName = "questionDataIDs")
    public String getQuestionDataIDsString()
    {
        return questionDataIDs;
    }

    public void setQuestionDataIDsString(String questionDataIDsString)
    {
        questionDataIDs = questionDataIDsString;
    }

    @DynamoDBIgnore
    public ArrayList<Long> getQuestionDataIDsArrayList()
    {
        String[] questionDataIDsArray = questionDataIDs.split("-");

        ArrayList<Long> questionDataIDsArrayList = (ArrayList)Arrays.asList(questionDataIDsArray);

        return questionDataIDsArrayList;
    }

    /*
    public ArrayList<QuestionData> getQuestionData()
    {
        LocalDBHandler localDB = new LocalDBHandler();

        ArrayList<Long> questionDataIDsArrayList = getQuestionDataIDsArrayList();
        ArrayList<QuestionData> questionData = new ArrayList<QuestionData>();

        for(Long questionDataID : questionDataIDsArrayList)
            questionData.add(localDB.getQuestionDataFromID(questionDataID));

        return questionData;
    }

    public boolean userAttemptedQuestion(long id, char answerChoiceSelected)
    {
        LocalDBHandler localDB = new LocalDBHandler();

        QuestionData questionData = new QuestionData(localDB.getQuestionFromID(id), answerChoiceSelected);

        localDB.setQuestionData(questionData);

        if(questionData.isQuestionCorrect())
            return true;
        return false;
    }
    */
    
    @DynamoDBIgnore
    public Student clone()
    {
        Student student = new Student(id, username, password, subjectIDs, questionDataIDs);
        return student;
    }
    
    @DynamoDBIgnore
    public String toString()
    {
        String ret = id + " " + username + " " + password + " " + subjectIDs + " " + questionDataIDs;
        return ret;
    }
}