Carrots Hunting
===============

## Description
A game which a rabbit catches carrots. Furthermore, there are archers who tries to capture the rabbit. 
Moreover, there are many special things like movement patterns and collision detections.

## Prerequisites

+ JRE
+ Greenfoot

## Usage
At first, clone or download this project. Afterwards, start the IDE `Greenfoot` and compile respectively interpret the project. 
Finally, you are able to play this game.

__NOTE__: In this game, the level is defined by a textfile `level.txt`:
```
{{""},{""},{""},{""},{""},{""},{""},{""},{""}},
{{""},{""},{""},{""},{""},{""},{""},{""},{""}},
{{""},{""},{""},{""},{""},{""},{""},{""},{""}},
{{""},{""},{""},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNW","GSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
{{"GSW","GSE"},{"GSWNE"},{"GNW","GNE","GSW"},{"GSWNE"},{"GNE","GSE"},{""},{""},{""},{""}},
{{"GNWSE"},{""},{""},{""},{"GNWSE"},{""},{""},{""},{""}},
{{"GSE","GNW"},{""},{""},{""},{"GSW","GSE","GNW"},{"GSWNE"},{"GSW","GNE"},{"GSWNE"},{"GNE"}},
{{"GNWSE"},{""},{""},{""},{"GNWSE"},{""},{""},{""},{""}},
{{"GSW","GNW"},{"GSWNE"},{"GSE","GNE","GSW"},{"GSWNE"},{"GNE","GNW"},{""},{""},{""},{""}},
{{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNW","GSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNWSE"},{""},{""},{""},{""},{""},{""}},
{{""},{""},{"GNW"},{""},{""},{""},{""},{""},{""}}
1
1
8 
1 
1
```
The first sequence represents the square of the game. The following parameters stands for (in the order) 
origin x, origin y, initial position, increment and direction of rotation regarding the cemetery.
Furthermore, there are a server which manages likes from users. In order to use the server, compile and 
interpret it. With the command `stop`, you are able to close the server.

## More information
Read more about Greenfoot at http://www.greenfoot.org/door.
