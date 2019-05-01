/***************************  LICENSE  *******************************
* This file is part of UBL.
* 
* UBL is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as 
* published by the Free Software Foundation, either version 3 of the 
* License, or (at your option) any later version.
* 
* UBL is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public 
* License along with UBL.  If not, see <http://www.gnu.org/licenses/>.
***********************************************************************/



import tom_kwiatkowski.ubl.learn.*;
import tom_kwiatkowski.ubl.lambda.*;
import tom_kwiatkowski.ubl.parser.*;

import java.io.*;

public class DevTrain extends Train {

    public static void main(String[] args){

	PType.addTypesFromFile("geo-funql.types");
	Lang.loadLangFromFile("geo-funql.lang");

	Train.emptyTest = true;

        int splitNum = Integer.parseInt(args[0]);
        DataSet train = new DataSet("data/geosents600-funql.ccg.dev.train."+splitNum);
	System.out.println("Train Size: "+train.size());
        DataSet test = new DataSet("data/geosents600-funql.ccg.dev.test."+splitNum);
	System.out.println("Test Size: "+test.size());
        LexiconFeatSet.loadCoOccCounts("data/geo-funql.dev.giza_probs");

	String fixedlex = args[1];
	Lexicon fixed = new Lexicon();
	if (!fixedlex.equals("none")){
	    fixed.addEntriesFromFile(fixedlex,true);
	}

	Train.EPOCHS=1;
	Train.alpha_0 = 1.0;
	Train.c = 0.00001;
	Train.maxSentLen=50;
	
	LexiconFeatSet.initWeightMultiplier = 10.0;
	LexiconFeatSet.initLexWeight = 10.0;

	Parser.pruneN=200;
      
	System.out.println("alpha_0 = "+Train.alpha_0);
	System.out.println("C = "+Train.c);
	System.out.println("initialMultiplier = "+LexiconFeatSet.initWeightMultiplier);
	System.out.println("NP init = "+LexiconFeatSet.initLexWeight);
	System.out.println("Parser beam  = "+Parser.pruneN);

	
	Train.verbose = true;

	Parser p = new Parser(fixed);
	p.makeFeatures();
	Train t = new Train();
	t.setDataSet(train);
	t.setTestSet(test);

	Train.verbose = true;
	t.stocGradTrain(p,true);		
	t.test(p,true);

  try {
    String lexicon = fixed.toStringWithScores();
    BufferedWriter writer = new BufferedWriter(new FileWriter("lexicon.txt", true));
    writer.write(lexicon);
    writer.close();
  } catch (IOException e) {
    System.err.println("Could not write lexicon.");
  }

    }
}
