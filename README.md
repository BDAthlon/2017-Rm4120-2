# 2017-Rm4120-2

## Description
Create an glob's game for biochemical reactions.

## Build Instructions
You need Java and Maven to run this code. Run mvn clean to install local dependencies. Run Rm4120-2-1.0.0-SNAPSHOT-jar-with-dependencies.jar.

## Run Instructions
The project was programmed in java and can be ran within an Eclipse workspace. By running the main() in GlobsRunner.java, a UI interface will load to show a model options panel on the left and a simulation panel on the right. The simulation panel is used to visualize the green globs that the player would like to build their model off of. The model options panel will first require the user to load an SBML model containing reactions, species, and parameters. Next, the text editor panel will load up a simplified version of the SBML fields for the user to modify the constant values that are assigned to the SBML models  so that the user can attempt to get their model to resemble the layout of where the green globs are set. To test the new model that they have made, the run button is used to plot the outcome of the user's model over the green glob. The user is allowed to make as many edits as they want, until they are satisfied that their plot is as close to the simulated green blob as possible. 

## License Agreement
All code contributed to this project is available to the public.
