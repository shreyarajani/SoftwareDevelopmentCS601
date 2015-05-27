package cs601.webmail.services;

import cs601.webmail.misc.Mail;
import cs601.webmail.protocols.NaiveBayesClassifier;

/**
 * Created by shreyarajani on 5/8/15.
 */
public class SpamService {
    public boolean checkForSpam(Mail mail) {
        initialTrain();
        String s = mail.getBody() + " " + mail.getBody();
        s = s.replace(".", " ");
        s=s.replace("&nbsp;", " ");
        NaiveBayesClassifier n = new NaiveBayesClassifier();
        return n.ifSpam(s);
    }

    private void initialTrain() {
        String spamString1 =
                "$$$ " +
                        "100% free " +
                        "Act Now " +
                        "Ad " +
                        "Affordable " +
                        "Amazing stuff ";
        String spamString2 =
                "Apply now " +
                        "Auto email removal " +
                        "Billion " +
                        "Cash bonus " +
                        "Cheap " +
                        "Collect child support " +
                        "Compare rates ";
        String spamString3 =
                "Compete for your business " +
                        "Credit " +
                        "Credit bureaus " +
                        "Dig up dirt on friends " +
                        "Double your income " +
                        "Earn $ " +
                        "Earn extra cash ";
        String spamString4 =
                "Eliminate debt " +
                        "Email marketing " +
                        "Explode your business " +
                        "Extra income " +
                        "F r e e " +
                        "Fast cash " +
                        "Financial freedom " +
                        "Financially independent ";
        String spamString5 =
                "Free " +
                        "Free gift " +
                        "Free grant money " +
                        "Free info " +
                        "Free installation " +
                        "Free investment " +
                        "Free leads " +
                        "Free membership " +
                        "Free offer ";
        String spamString6 =
                "Free preview Guarantee " +
                        "‘Hidden’ assets " +
                        "Home based " +
                        "Homebased business " +
                        "Income from home " +
                        "Increase sales " +
                        "Increase traffic ";
        String spamString7 =
                "Increase your sales " +
                        "Incredible deal " +
                        "Info you requested " +
                        "Information you requested " +
                        "Internet market " +
                        "Leave ";
        String spamString8 =
                "Limited time offer " +
                        "Make $ " +
                        "Mortgage Rates " +
                        "Multi level marketing " +
                        "No investment " +
                        "Obligation " +
                        "Online marketing " +
                        "Opportunity ";
        String spamString9 =
                "Order Now " +
                        "Prices " +
                        "Promise you " +
                        "Refinance " +
                        "Remove " +
                        "Reverses aging " +
                        "Save $ ";
        String spamString10 =
                "Search engine listings " +
                        "Serious cash " +
                        "Stock disclaimer statement " +
                        "Stop snoring " +
                        "Thousands " +
                        "Unsubscribe " +
                        "Web traffic " +
                        "Weight loss";

        NaiveBayesClassifier n = new NaiveBayesClassifier();
        n.trainForSpam(spamString1);
        n.trainForSpam(spamString2);
        n.trainForSpam(spamString3);
        n.trainForSpam(spamString4);
        n.trainForSpam(spamString5);
        n.trainForSpam(spamString6);
        n.trainForSpam(spamString7);
        n.trainForSpam(spamString8);
        n.trainForSpam(spamString9);
        n.trainForSpam(spamString10);

    }
}
