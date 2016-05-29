package aptestpracticeappdbwriter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.ArrayList;
import java.util.Arrays;

@DynamoDBTable(tableName = "Questions")
public class Question
{
    public long id;
    public String qText;
    public String answerChoiceIDs;
    public int correctAnswerChoice;

    public Question()
    {

    }

    public Question(long id, String qText, String answerChoiceIDs, int correctAnswerChoice)
    {
        this.id = id;
        this.qText = qText;
        this.answerChoiceIDs = answerChoiceIDs;
        this.correctAnswerChoice = correctAnswerChoice;
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

    @DynamoDBAttribute(attributeName = "qText")
    public String getQText()
    {
        return qText;
    }

    public void setQText(String qText)
    {
        this.qText = qText;
    }

    @DynamoDBAttribute(attributeName = "answerChoiceIDs")
    public String getAnswerChoiceIDsString()
    {
        return answerChoiceIDs;
    }

    public void setAnswerChoiceIDsString(String answerChoiceIDsString)
    {
        answerChoiceIDs = answerChoiceIDsString;
    }

    @DynamoDBIgnore
    public ArrayList<Long> getAnswerChoiceIDsArrayList()
    {
        String[] answerChoiceIDsArray = answerChoiceIDs.split("-");

        ArrayList<Long> answerChoiceIDsArrayList = (ArrayList) Arrays.asList(answerChoiceIDsArray);

        return answerChoiceIDsArrayList;
    }

    /*
    public ArrayList<AnswerChoice> getAnswerChoices()
    {
        LocalDBHandler localDB = new LocalDBHandler();

        ArrayList<Long> answerChoiceIDsArrayList = getAnswerChoiceIDsArrayList();
        ArrayList<AnswerChoice> answerChoices = new ArrayList<AnswerChoice>();

        for(Long answerChoiceID : answerChoiceIDsArrayList)
            answerChoices.add(localDB.getAnswerChoiceFromID(answerChoiceID));

        return answerChoices;
    }
    */

    @DynamoDBAttribute(attributeName = "correctAnswerChoice")
    public int getCorrectAnswerChoice()
    {
        return correctAnswerChoice;
    }

    public void setCorrectAnswerChoice(int correctAnswerChoice)
    {
        this.correctAnswerChoice = correctAnswerChoice;
    }
    
    @DynamoDBIgnore
    public Question clone()
    {
        Question question = new Question(id, qText, answerChoiceIDs, correctAnswerChoice);
        return question;
    }
    
    @DynamoDBIgnore
    public String toString()
    {
        String ret = id + " " + qText + " " + answerChoiceIDs + " " + correctAnswerChoice;
        return ret;
    }
}