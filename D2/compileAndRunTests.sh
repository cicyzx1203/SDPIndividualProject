#!/bin/sh
\rm -f report.txt
javac -cp lib/textsummary.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar -d testclasses testsrc/edu/gatech/seclass/textsummary/*
cd testclasses
jar -cf tests.jar edu/gatech/seclass/textsummary/MyMainTest.class
cd ..
cp testclasses/tests.jar lib/tests.jar
rm testclasses/tests.jar
java -cp lib/tests.jar:testclasses:lib/textsummary.jar:lib/junit-4.12.jar:lib/hamcrest-core-1.3.jar org.junit.runner.JUnitCore edu.gatech.seclass.textsummary.MyMainTest > report.txt
