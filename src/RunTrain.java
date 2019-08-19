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
import java.util.HashMap;

class Config {
  public String typeFile = "geo-lambda.types";
  public String langFile = "geo-lambda.lang";
  public String trainFile = "data/geosents600-funql.ccg.dev.train.";
  public String testFile = "data/geosents600-funql.ccg.dev.test.";
  public String gizaFile = "data/geo-funql.dev.giza_probs";
  public String outputFile = "lexicon.txt";
  public String fixedlex = "none";
  public int splitNum = 1;
  public int epochs = 1;
  public double alpha = 1.0;
  public double c = 0.00001;
  public int maxSentLength = 50;
  public double initWeightMultiplier = 10.0;
  public double initLexWeight = 10.0;
  public int pruneN = 200;

  public Config() { }

  public Config(HashMap<String, String> arguments) {
    String lf = arguments.getOrDefault("--lf", "funql");
    String corpus = arguments.getOrDefault("--corpus", "geo880");
    String root = arguments.getOrDefault("--root", 
        "experiments/" + corpus + "-" + lf + "/");
    String splitString = arguments.getOrDefault("--split", "1");
    splitNum = Integer.parseInt(splitString);
    if (lf == "funql") {
      gizaFile = arguments.getOrDefault("--probs", 
          root + "data/geo-funql.dev.giza_probs");
    } else {
      gizaFile = arguments.getOrDefault("--probs",
          root + "data/geo600.dev.giza_probs");
    }
    outputFile = arguments.getOrDefault("--output", "lexicon.txt");
    fixedlex = arguments.getOrDefault("--fixedlex", "none");
    String epochString = arguments.getOrDefault("--epochs", "20");
    epochs = Integer.parseInt(epochString);
    String alphaString = arguments.getOrDefault("--alpha", "1.0");
    alpha = Double.parseDouble(alphaString);
    String cString = arguments.getOrDefault("--c", "0.00001");
    c = Double.parseDouble(cString);
    String maxlenString = arguments.getOrDefault("--maxlen", "50");
    maxSentLength = Integer.parseInt(maxlenString);
    String multiplierString = arguments.getOrDefault("--multiplier", "10.0");
    initWeightMultiplier = Double.parseDouble(multiplierString);
    String weightString = arguments.getOrDefault("--lexiconweight", "10.0");
    initLexWeight = Double.parseDouble(weightString);
    String pruneString = arguments.getOrDefault("--prune", "200");
    pruneN = Integer.parseInt(pruneString);

    if (lf == "funql") {
      typeFile = root + "geo-funql.types";
      langFile = root + "geo-funql.lang";
    } else {
      typeFile = root + "geo-lambda.types";
      langFile = root + "geo-lambda.lang";
    }

    String task = arguments.getOrDefault("--task", "dev");
    if (task == "dev" && lf == "funql") {
      trainFile = root + "data/geosents600-funql.ccg.dev.train." + splitNum;
      testFile = root + "data/geosents600-funql.ccg.dev.test." + splitNum;
    } else if (task == "test" && lf == "funql") {
      trainFile = root + "data/geo880-funql.train";
      testFile = root + "data/geo880-funql.test";
    } else if (task == "dev" && lf == "lambda") {
      trainFile = root + "data/geosents600-typed.ccg.dev.train." + splitNum;
      testFile = root + "data/geosents600-typed.ccg.dev.test." + splitNum;
    } else {
      trainFile = root + "data/geosents600-typed.ccg.dev";
      testFile = root + "data/geosents280-typed.ccg.test";
    }
  }
}

public class RunTrain extends Train {

  public static void main(String[] args) {
    HashMap<String, String> argMap = new HashMap<>();
    for (String arg: args) {
      String[] parts = arg.split("=");
      argMap.put(parts[0], parts[1]);
    }
    Config config = new Config(argMap);

    PType.addTypesFromFile(config.typeFile);
    Lang.loadLangFromFile(config.langFile);

    Train.emptyTest = true;

    DataSet train = new DataSet(config.trainFile);
    System.out.println("Train Size: " + train.size());
    DataSet test = new DataSet(config.testFile);
    System.out.println("Test Size: " + test.size());
    LexiconFeatSet.loadCoOccCounts(config.gizaFile);

    String fixedlex = config.fixedlex;
    Lexicon fixed = new Lexicon();
    if (!fixedlex.equals("none")) {
      fixed.addEntriesFromFile(fixedlex, true);
    }

    Train.EPOCHS = config.epochs;
    Train.alpha_0 = config.alpha;
    Train.c = config.c;
    Train.maxSentLen = config.maxSentLength;

    LexiconFeatSet.initWeightMultiplier = config.initWeightMultiplier;
    LexiconFeatSet.initLexWeight = config.initLexWeight;

    Parser.pruneN = config.pruneN;

    System.out.println("alpha_0 = " + Train.alpha_0);
    System.out.println("C = " + Train.c);
    System.out.println("initialMultiplier = " + LexiconFeatSet.initWeightMultiplier);
    System.out.println("NP init = " + LexiconFeatSet.initLexWeight);
    System.out.println("Parser beam = " + Parser.pruneN);

    Train.verbose = true;

    Parser p = new Parser(fixed);
    p.makeFeatures();
    Train t = new Train();
    t.setDataSet(train);
    t.setTestSet(test);

    Train.verbose = true;
    t.stocGradTrain(p, true);
    t.test(p, true);

    try {
      String lexicon = fixed.toStringWithScores();
      BufferedWriter writer = new BufferedWriter(new FileWriter(config.outputFile, true));
      writer.write(lexicon);
      writer.close();
    } catch (IOException e) {
      System.err.println("Could not write lexicon.");
    }
  }
}
