college of computing - computer science
cop4620 - constructors of language translators
ronald shevchenko - n01385011
dr. eggan
due: 03/07/2019
submitted: 03/06/2019

this program makes use of program 1 (or lexical analyzer) and creates the acception/
rejection of the input program based on the provided grammar in the textbook that has
been reduced, left recursion removed, and left factoring removed, to make it work with
firsts and follows. the ll(1) parser has been implemented in java.

to run, simply type make to compile the project
then p2 fn to run the project, where fn is the input file

once this has been done, it will print 'ACCEPT' or 'REJECT' whether the given program
follows the reduced grammar

it will create the files: Analyzer.class
			  p2.class
			  Token.class
			  Syntax.class
it will consume the file you provided, for example: "testfile.txt"
