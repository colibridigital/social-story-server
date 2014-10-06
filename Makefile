all:
	mvn clean install site | egrep -v "(^\[INFO\]|^\[DEBUG\])"
