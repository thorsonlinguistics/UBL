# Unification-Based Learning 

This is a copy of code from Tom Kwiatkowski's page at Edinburgh, which no
longer exists. It is forked from Jim White's [Github
repository](https://github.com/jimwhite/UBL). The code corresponds to Tom
Kwiatkowski's 2010 EMNLP paper:

Inducing probabilistic CCG grammars from logical form with higher-order
unification. Tom Kwiatkowski, Luke Zettlemoyer, Sharon Goldwater and Mark
Steedman. Proceedings of the Conference on Empirical Methods in Natural
Language Processing (EMNLP), Cambridge, MA, 2010
http://www.aclweb.org/anthology/D10-1119.pdf

This fork includes a few changes to make it easier to run the experiments.

## Building

You will need to have a Java Development Kit in order to build the application
and a Java Runtime Environment to run the experiments. 

The following command can be used to build the application from the root of the
repository:

    javac -classpath src src/tom_kwiatkowski/ubl/**/*.java src/RunTrain.java

## Running the Experiments

The default experiment can be run using the following command:

    java -classpath src RunTrain

In addition, the following arguments can be used to configure the experiments.
All of the command-line arguments are of the form --argument=value.

### --lf

Sets the logical form to be used, which automatically sets the data root to the
appropriate directory for training and testing. Possible values are funql and
lambda.

### --corpus

Sets the corpus to be used, which automatically sets the data root to the
appropriate directory for training and testing. Currently only geo880 is
supported with the main script.

### --root

The root directory containing the data. This should be one of the directories
immediately under "experiments".

### --task

Set to either test or dev to determine the data split to be used.

### --split

If running the dev experiments, the cross-validation split to use.

### --probs

The file (relative to the root directory) containing the GIZA probs.

### --fixedlex

The file containing the fixed lexicon. 

### --output

The name of the output file containing the resulting lexicon.

### --epochs

The number of training epochs to use.

### --alpha

Alpha0, used to define the temperature of parameter updates.

### --c

C, used to define the temperature of parameter updates.

### --maxlen

The maximum sentence length in processing.

### --multiplier

The initial weight multiplier.

### --lexiconweight

The initial lexicon weight.

### --prune

The maximum cell size for pruning.
