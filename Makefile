# Makefile for MeanFilterSerial and MeanFilterParallel

# Compiler
JAVAC = javac
# Java files
SOURCES = MeanFilterSerial.java MeanFilterParallel.java
# Class files
CLASSES = $(SOURCES:.java=.class)

# Default target
all: MeanFilterSerial MeanFilterParallel

# Compile MeanFilterSeral
MeanFilterSerial: MeanFilterSerial.class

MeanFilterSerial.class: MeanFilterSerial.java
	$(JAVAC) MeanFilterSerial.java

# Compile MeanFilterParallel
MeanFilterParallel: MeanFilterParallel.class

MeanFilterParallel.class: MeanFilterParallel.java
	$(JAVAC) MeanFilterParallel.java

# Run MeanFilterSerial
run-serial: MeanFilterSerial
	java MeanFilterSerial

# Run MeanFilterParallel
run-parallel: MeanFilterParallel
	java MeanFilterParallel

# Clean up class files and output images
clean:
	rm -f $(CLASSES)