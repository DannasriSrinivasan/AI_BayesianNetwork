import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class bnet {

	public  Map<String, Double> burglary = new HashMap<String, Double>();
	public  Map<String, Double> earthquake = new HashMap<String, Double>();
	public  Map<String, Double> alarm = new HashMap<String, Double>();
	public  Map<String, Double> john = new HashMap<String, Double>();
	public  Map<String, Double> mary = new HashMap<String, Double>();
	public  Map<Character, ArrayList<Character>> numerator = new HashMap<>();
	public  Map<Character, ArrayList<Character>> denominator = new HashMap<>();

	public void generateBayesianNetwork() {
		burglary.put("BT", 0.001);
		earthquake.put("ET", 0.002);
		alarm.put("ATBT,ET", 0.95);
		alarm.put("ATBT,EF", 0.94);
		alarm.put("ATBF,ET", 0.29);
		alarm.put("ATBF,EF", 0.001);
		john.put("JTAT", 0.90);		
		john.put("JTAF", 0.05);
		mary.put("MTAT", 0.70);		
		mary.put("MTAF", 0.01);
	}
	
	public double computeProbability(char burglaryIp,char earthquakeIp,char alarmIp,char johnIp,char maryIp) {
		double computedProbability=0; 
		double burglaryVal=0,earthquakeVal=0,alarmVal=0,johnVal=0,maryVal=0;
		burglaryVal=burglary.get("BT");
		earthquakeVal=earthquake.get("ET");
		alarmVal=alarm.get("ATB"+burglaryIp+",E"+earthquakeIp);
		johnVal=john.get("JTA"+alarmIp);
		maryVal=mary.get("MTA"+alarmIp);
		if(burglaryIp=='F') burglaryVal=1-burglaryVal;
		if(earthquakeIp=='F') earthquakeVal=1-earthquakeVal;
		if(alarmIp=='F') alarmVal=1-alarmVal;	
		if(johnIp=='F') johnVal=1-johnVal;
		if(maryIp=='F') maryVal=1-maryVal;
		computedProbability=burglaryVal * earthquakeVal* alarmVal *johnVal *maryVal;
		return computedProbability;
	}


	public Map<Character, ArrayList<Character>> generateParents(Map<Character, ArrayList<Character>> givenMap){
		Map<Character, ArrayList<Character>> booleval=new HashMap<Character, ArrayList<Character>>();
		ArrayList<Character> boole;
		if(!(givenMap.containsKey('B'))){
			boole= new ArrayList<Character>();
			boole.add('T');
			boole.add('F');
			booleval.put('B', boole);
		}
		if(!(givenMap.containsKey('E'))){
			boole= new ArrayList<Character>();
			boole.add('T');
			boole.add('F');
			booleval.put('E', boole);
		}
		if(!(givenMap.containsKey('A'))){
			boole= new ArrayList<Character>();
			boole.add('T');
			boole.add('F');
			booleval.put('A', boole);
		}
		if(!(givenMap.containsKey('J'))){
			boole= new ArrayList<Character>();
			boole.add('T');
			boole.add('F');
			booleval.put('J', boole);
		}
		if(!(givenMap.containsKey('M'))){
			boole= new ArrayList<Character>();
			boole.add('T');
			boole.add('F');
			booleval.put('M', boole);
		}
		return booleval;

	}
	
	public double calculateProbalitiesSum(Map<Character, ArrayList<Character>> inputMap) {
		double problalitySum = 0;
		for(int bur=0; bur<inputMap.get('B').size(); bur++) {
			for(int earth=0; earth<inputMap.get('E').size(); earth++) {
				for(int alarm=0; alarm<inputMap.get('A').size(); alarm++) {
					for(int john=0; john<inputMap.get('J').size(); john++) {
						for(int mary=0; mary<inputMap.get('M').size(); mary++) {
							problalitySum += computeProbability(inputMap.get('B').get(bur), inputMap.get('E').get(earth), inputMap.get('A').get(alarm), inputMap.get('J').get(john), inputMap.get('M').get(mary));
						}
					}
				}
			}
		}
		return problalitySum;
	}
	
	public static void main(String[] args) {
		bnet bnetObj = new bnet();
		double numeratorValue = 0;
		double denomiatorValue = 0;
		double probability = 0;
		if(args.length < 1 || args.length > 6) {
			System.out.println("In general, bnet takes 1 to 6(no more, no fewer)");
			System.exit(0);
		}
		//Getting inputs form command line
		int givenCheck = 0;
		for(String a: args) {
			ArrayList<Character> boole=new ArrayList<Character>();
			if(a.equals("given")) {
				givenCheck=1;
				continue;
			}
			if(givenCheck==0) {
				String temp=a.toUpperCase();
				boole.add(temp.charAt(1));
				bnetObj.numerator.put(temp.charAt(0), boole);
			}
			else {
				String temp=a.toUpperCase();
				boole.add(temp.charAt(1));
				bnetObj.denominator.put(temp.charAt(0), boole);
			}
		}
		//Hardcoding bayesian network inputs
		bnetObj.generateBayesianNetwork();
		//genarating probabilities
		bnetObj.numerator.putAll(bnetObj.denominator);
		bnetObj.numerator.putAll(bnetObj.generateParents(bnetObj.numerator));
		
		numeratorValue = bnetObj.calculateProbalitiesSum(bnetObj.numerator);
		if(givenCheck == 1) {
			bnetObj.denominator.putAll(bnetObj.generateParents(bnetObj.denominator));
			denomiatorValue = bnetObj.calculateProbalitiesSum(bnetObj.denominator);
			probability = numeratorValue/denomiatorValue;
		}else {
			probability = numeratorValue;
		}
		System.out.println("Probability =  "+probability);
	}
}
