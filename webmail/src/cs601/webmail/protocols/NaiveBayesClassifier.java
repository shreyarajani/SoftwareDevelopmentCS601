package cs601.webmail.protocols;

import net.sf.classifier4J.ClassifierException;
import net.sf.classifier4J.IClassifier;
import net.sf.classifier4J.bayesian.BayesianClassifier;
import net.sf.classifier4J.bayesian.IWordsDataSource;
import net.sf.classifier4J.bayesian.SimpleWordsDataSource;
import net.sf.classifier4J.bayesian.WordsDataSourceException;
import org.apache.log4j.Logger;

/**
 * Created by shreyarajani on 5/8/15.
 */
public class NaiveBayesClassifier {
    static final Logger logger = Logger.getLogger(NaiveBayesClassifier.class);
    private static IWordsDataSource wds = new SimpleWordsDataSource();
    private static IClassifier classifier = new BayesianClassifier(wds);

    public void trainForSpam(String s) {
        s=s.toLowerCase();
        for (String sample : s.split("\\s")) {
            try {
                wds.addMatch(sample);
            } catch (WordsDataSourceException e) {
                logger.error(e);
            }
        }
    }

    public boolean ifSpam(String s) {
        double value;
        try {
            value = classifier.classify(s);
            if (value > 0.6) {
                trainForSpam(s);
                return true;
            }
        } catch (ClassifierException e) {
            logger.error(e);
        }
        return false;
    }
}
