package cs601.webmail.test;
/**
 * Created by shreyarajani on 4/26/15.
 */

import cs601.webmail.managers.ErrorManager;
import net.sf.classifier4J.ClassifierException;
import net.sf.classifier4J.IClassifier;
import net.sf.classifier4J.bayesian.BayesianClassifier;
import net.sf.classifier4J.bayesian.IWordsDataSource;
import net.sf.classifier4J.bayesian.SimpleWordsDataSource;
import net.sf.classifier4J.bayesian.WordsDataSourceException;

public class Classifier {
    private static IWordsDataSource wds = new SimpleWordsDataSource();
    private static IClassifier classifier = new BayesianClassifier(wds);
    private static boolean isTrainned = false;

    public Classifier() {
        if (!isTrainned) {
            preTrain();
            isTrainned = true;
        }
    }

    public void trainForSpam(String text) {
        for (String s : text.toLowerCase().split("\\s")) {
            try {
                wds.addMatch(s);
            } catch (WordsDataSourceException e) {
                ErrorManager.instance().error(getClass().getName(), e);
            }
        }
    }

    public void trainHam(String text) {
        for (String sample : text.toLowerCase().split("\\s")) {
            try {
                wds.addNonMatch(sample);
            } catch (WordsDataSourceException e) {
                ErrorManager.instance().error(getClass().getName(), e);
            }
        }
    }

    public void preTrain() {

        final String sample = "spam";
        trainForSpam(sample);

    }

    public boolean isSpam(String email) {
        try {
            double value = classifier.classify(email);
            if (value > 0.80) {
                return true;
            }
        } catch (ClassifierException e) {
            ErrorManager.instance().error(getClass().getName(), e);
        }
        return false;
    }

    public static void main(String[] args) {
        Classifier classifier = new Classifier();
        if (classifier.isSpam("Hello this is me!")) {
            System.out.println("This is spam!");
        }
        else{
            System.out.println("not Spam");
        }
    }
}
