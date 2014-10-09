test:
	gradle --daemon test

check:
	gradle --daemon clean check cobertura buildDashboard

all:
	mvn clean install site | egrep -v "(^\[INFO\]|^\[DEBUG\])"
