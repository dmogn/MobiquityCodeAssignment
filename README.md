# MobiquityCodeAssignment

Mobiquity Challenge



Usage: java com.mobiquityinc.packer.Packer [input text file]

Requirements: JDK 8+, Maven


Algorithm description

The algorithm runs in 3 steps:

1. Sort items in the package by specific price (price/weight) in desc order.

2. Build the sequence in recursive function, with adding or avoiding an item in the sequence. 

3. Sort result by index incrementation, print to text output.


Steps 1 and 3 are unnecessary and added for complexity optimization of step 2. Sorting complexity is O(N ln N) and step 2 worst case complexity is O(N^2).
