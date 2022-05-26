# expert-waffle

**a recreation of the game Harbor Master in java**

## Graphics

#### Boats

|small|medium|large|
|-|-|-|
|<img src="/Harbor-Master/src/imgs/boat1-0.png" width=50>|<img src="/Harbor-Master/src/imgs/boat2-0.png" width=50>|<img src="/Harbor-Master/src/imgs/boat4-0.png" width=50>|

#### Maps

|Sandy Shores|Rocky Ridges|The Raft|
|-|-|-|
|<img src="/Harbor-Master/src/imgs/map1.png" width=200>|<img src="/Harbor-Master/src/imgs/map2.png" width=200>|<img src="/Harbor-Master/src/imgs/map3.png" width=200>|

#### Screens
|Title|Map Selection|Game Over|
|-|-|-|
|<img src="/Harbor-Master/src/imgs/Title.png" width=200>|<img src="/Harbor-Master/src/imgs/MapSelect.png" width=200>|<img src="/Harbor-Master/src/imgs/GameOver_Screen.png" width=200>|

## Notable Classes

#### RotatingPicture
- like a regular `Picture`, but stores an angle and rotates the loaded image

##### Boat
- extends `RotatingPicture` and defines boat-related variables and definitions
- uses an arraylist of `Position` to define its next moves
- stores `Cargo` and `DockInfo` information

#### Game
- defines a game
- includes a `Map` and an arraylist of `Boat`
- also includes booleans to define the status of the game
- spawns in new boats
- handles painting all other objects

#### MapEditor
- **unstable**, but can be used to add/edit map definitions
- a map is defined with an image (in `src/imgs/`) and a txt file (in `src/map-sectors`)
