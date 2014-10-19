test:
	gradle --daemon test

check:
	gradle --daemon clean check buildDashboard

all:
	mvn clean install site | egrep -v "(^\[INFO\]|^\[DEBUG\])"
