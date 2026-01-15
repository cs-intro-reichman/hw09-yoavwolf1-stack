import java.util.HashMap;
import java.util.Random;

import javax.swing.plaf.synth.SynthDesktopIconUI;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		In in = new In(fileName);
        String window = "";
        char ch;
        List curr;
        for(int i = 0; i < windowLength && ! in.isEmpty(); i++)
            window += in.readChar();

        while(! in.isEmpty()) {
            ch = in.readChar();
            curr = CharDataMap.get(window);
            if(curr != null) {
                curr.update(ch);
            } else {
                List lis = new List();
                lis.addFirst(ch);
                CharDataMap.put(window, lis);
            }
            window = window.substring(1) + ch;
        }
        for(List probs : CharDataMap.values())
            this.calculateProbabilities(probs);
	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	void calculateProbabilities(List probs) {				
		int letters = 0;
        int totalCount = 0;
        CharData curr;
        ListIterator iter = probs.listIterator(0);
        while(iter.hasNext())
            letters += iter.next().count;

        iter = probs.listIterator(0);
        while(iter.hasNext()) {
            curr = iter.next();
            curr.p = (double)(curr.count) / letters;
            totalCount += curr.count;
            curr.cp = (double)(totalCount) / letters;
        }
	}

    // Returns a random character from the given probabilities list.
	char getRandomChar(List probs) {
		double rand = randomGenerator.nextDouble();
        CharData curr;
        ListIterator iter = probs.listIterator(0);
        while(iter.hasNext()) {
            curr = iter.next();
            if(curr.cp >= rand)
                return curr.chr;
        }
        return ' ';
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		if(initialText.length() < windowLength) return initialText;
        String window = initialText.substring(initialText.length() - windowLength);
        String output = initialText;
        List curr;
        char next;
        for(int i = 0; i < textLength; i++) {
            curr = CharDataMap.get(window);
            if(curr == null)
                return output;
            next = this.getRandomChar(curr);
            output += next;
            window = window.substring(1) + next;
        }
        return output;
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        int windowLeng = Integer.parseInt(args[0]);
        String initalText = args[1];
        int textLength = Integer.parseInt(args[2]);
        String fixORrand = args[3];
        String fileName = args[4];
        LanguageModel lang;

        if(fixORrand.equals("random"))
            lang = new LanguageModel(windowLeng);
        else
            lang = new LanguageModel(windowLeng, 20);

        lang.train(fileName);
        System.out.println(lang.generate(initalText, textLength));
    }
}