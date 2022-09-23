# Caro-Game

Trang Ngo

**1. About:** 

Caro, also called five-in-a-row, is a popular Vietnamese logic game. Game rule is similar to Tic-Tac-Toe; however, a player needs 5 move in a row to win instead of 3, and the board on which the game is played on can be of any size, not just a 3-by-3 board. Players take turn placing moves on available spaces on a grid, and whoever reach 5 moves consecutively (in horizontal, vertical, or diagonal direction) first win the game. 

This game is popular among Vietnamese students in between classes, on spare paper in notebooks. Therefore, the board size is not limited and can be any size. 
This project was created for me to practice concepts of Object Oriented Programming and Artificial Intelligence. 

![alt text](https://images.tuyensinh247.com/picture/article/2012/1027/chieu-tro-cua-hoc-sinh-khi-luoi-nghe-giang_2.jpg)

**2. Features:**

Game Play:
The game is usually played on a grid, representing in this project by a 2D matrix. A move is made on a cell (intersection between a specific row and column) within the matrix. 

There are 2 players, represented in this project as Player X and player O. Player X start by placing a move on untaken spot within the bounds of the board. Then, player O makes a valid move in another untaken spot on the board. The acting player switches back between the two players until a win condition is reach. The win condition is that a player has 5 moves in a row on the board (meant in the consecutive sense, the 5 moves can line up horizontally, vertically, or diagonally). There are other variants, including that the win condition is not met if the 5 moves are not immediately blocked by the opposing player on both side. That rule will be implemented in a future version of this project.  

![alt text](https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRe_aFzziDM5y1A9MZVbZgdkyOtOTR5QncZoScPSxHsoApYkyWOkiworiDOc6ivekbAtVE&usqp=CAU)

In the image above, win condition for player X is met. 



Main classes:
* Game: implement the game play for Caro.
* GameBoard: represent the game board (inluding moves made) and have methods to extract certain information from current board state.
* Player: represent a player in the game.

Unit tests for GameBoard class are included in this project. 


**3. How to use this project:**

Run ./test/GameTest.java to start the game. The command line will prompt users for the row and column of moves to be made, and then print out current board state. The command prompt will continue asking for moves until the entire board is filled or a player reaches win condition. 

**4. Current limitations**

* Game board size cannot be larger than 99 by 99. 
* Players need to input in the cooridnate of moves to make, which can be a hassle.


**5. Future ideas:**

* Create an AI to play the game using mini-max algorithms or reinforcement learning.
* Implement game variant in which a 5-in-a-row which is blocked on both side does not win the game.
* Implement a GUI for the game so players can click on positions on game board instead of having to input move's coordinates from command line.  
