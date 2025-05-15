/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/*
MARK REUBEN E. FORTUITO - DOCUMENT SPECIALIST
JOSHUA P. GAGARIN - BUSINESS ANALYST
LEI AIREL A. LOQUIS - SYSTEM ANALYST
JOHN CHRISTOFER LUCERO - ASSISTANT PROGRAMMER
JEREMY L. MALANA - LEAD PROGRAMMER
MIGUEL ROSE N. SORIANO - PROJECT MANAGER
*/
package Customer;
import javax.swing.JTextArea;

/**
 *
 * @author Jeremy Malana
 */
public class ChatBot {
    
    private static final String Q1 = "What are the store hours?";
    private static final String A1 = "Nagimas is open daily from 8AM TO 9PM. Operating hours may vary on holidays.";
    
    private static final String Q2 = "How can I place an order?";
    private static final String A2 = "Customers can place an order through the Nagimas Ordering System by selecting their preferred items and confirming their order.";
    
    private static final String Q3 = "Can I order without registering?";
    private static final String A3 = "Yes! The system offers a Guest Mode option, allowing customers to browse and place orders without creating an account.";
    
    private static final String Q4 = "How do I track my order?";
    private static final String A4 = "Customers can check their order status through the Customer Account Dashboard. Updates will be provided in real-time.";
    
    private static final String Q5 = "What payment methods are accepted?";
    private static final String A5 = "Currently, Nagimas accepts Cash on Cashier.";
    
    private static final String Q6 = "How can I modify or cancel my order?";
    private static final String A6 = "Orders can be modified or canceled before confirmation. Once an order is processed, cancellations may no longer be possible.";
    
    private static final String Q7 = "Is delivery available?";
    private static final String A7 = "No, only pick up from the store.";
    
    private static final String Q8 = "How do I contact the store for further inquiries?";
    private static final String A8 = "For additional questions, customers Email Us in the nagimaslittlegoodtaste@gmail.com.";
    
    public void displayAnswer(int questionNumber, JTextArea textArea) {
        String question = getQuestion(questionNumber);
        String answer = getAnswer(questionNumber);
        
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Question: ").append(question).append("\n\n");
        messageBuilder.append("Answer: ").append(answer);
     
        textArea.setText(messageBuilder.toString());
        
        textArea.setCaretPosition(0);
    }
    
    public String getQuestion(int questionNumber) {
        switch (questionNumber) {
            case 1: return Q1;
            case 2: return Q2;
            case 3: return Q3;
            case 4: return Q4;
            case 5: return Q5;
            case 6: return Q6;
            case 7: return Q7;
            case 8: return Q8;
            default: return "Unknown question";
        }
    }
    
    public String getAnswer(int questionNumber) {
        switch (questionNumber) {
            case 1: return A1;
            case 2: return A2;
            case 3: return A3;
            case 4: return A4;
            case 5: return A5;
            case 6: return A6;
            case 7: return A7;
            case 8: return A8;
            default: return "Sorry, I don't have an answer for that question.";
        }
    }
    
    public void displayWelcomeMessage(JTextArea textArea) {
        StringBuilder welcomeMessage = new StringBuilder();
        welcomeMessage.append("Welcome to Nagimas Chat Bot!\n\n");
        welcomeMessage.append("Please click on any of the frequently asked questions to see the answer.\n\n");
        welcomeMessage.append("Use the questions on the Above to navigate through our FAQs.");
        
        textArea.setText(welcomeMessage.toString());
        textArea.setCaretPosition(0);
    }
}
