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

## Running the experiments

There are two parameters for the experiments:

- Dataset: either geo250 or geo880. geo250 is smaller, but includes sentences
  in multiple languages.
- Logical form: either funql (the GeoQuery functional query language) or lambda
  (the lambda calculus).

Each folder contains scripts for compiling and running the experiments, as well
as further instructions.
