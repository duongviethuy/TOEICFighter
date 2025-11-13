package project.toeicfighter.cores;

import project.toeicfighter.models.Account;
import project.toeicfighter.models.Question;

public class SessionManager {
    private static Account currentAccount;
    private static Question HandlingQuestion;

    public static void setCurrentAccount(Account account) {
        currentAccount = account;
    }

    public static Account getCurrentAccount() {
        return currentAccount;
    }

    public static void clearSession() {
        currentAccount = null;
    }

    public static Question getHandlingQuestion() {
        return HandlingQuestion;
    }

    public static void setHandlingQuestion(Question handlingQuestion) {
        HandlingQuestion = handlingQuestion;
    }

    public static void clearHandlingQuestion(Question handlingQuestion) {
        HandlingQuestion = null;
    }
}
