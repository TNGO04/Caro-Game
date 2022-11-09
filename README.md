# Caro-Game

Thuy Trang Ngo

## About

Caro, also called five-in-a-row, is a popular Vietnamese logic game. Game rule is similar to Tic-Tac-Toe; however, a player needs 5 move in a row to win instead of 3, and the board on which the game is played on can be of any size, not just a 3-by-3 board. Players take turn placing moves on available spaces on a grid, and whoever reach 5 moves consecutively (in horizontal, vertical, or diagonal direction) first win the game. 

This game is popular among Vietnamese students in between classes, on spare paper in notebooks. Therefore, the board size is not limited and can be any size. 
This project was created for me to practice concepts of Object Oriented Programming and Artificial Intelligence. 

  <p align="center">
  <img src="https://images.tuyensinh247.com/picture/article/2012/1027/chieu-tro-cua-hoc-sinh-khi-luoi-nghe-giang_2.jpg" />
</p>


## Features

Game Play:
The game is usually played on a grid, representing in this project by a 2D matrix. A move is made on a cell (intersection between a specific row and column) within the matrix. 

There are 2 players, represented in this project as Player X and player O. Player X start by placing a move on untaken spot within the bounds of the board. Then, player O makes a valid move in another untaken spot on the board. The acting player switches back between the two players until a win condition is reach. The win condition is that a player has 5 moves in a row on the board (meant in the consecutive sense, the 5 moves can line up horizontally, vertically, or diagonally). There are other variants, including that the win condition is not met if the 5 moves are not immediately blocked by the opposing player on both side. That rule will be implemented in a future version of this project.  

<p align="center">
  <img src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRe_aFzziDM5y1A9MZVbZgdkyOtOTR5QncZoScPSxHsoApYkyWOkiworiDOc6ivekbAtVE&usqp=CAU" />
</p>

<p align="center">
<i> In the image above, win condition for player X is met </i>
</p>




## Project Structure
**1) Game:** implement the game play for Caro, including setting up players and gameboard, as well as simulating game play between players, checking for win condition, and terminating the game. 

**2) Player:** represent a player in the game, stores a char that represents the player on board. 

**3) Package board:**

 * *GameBoard:* represent the game board as a 2D matrix of characters (representing moves made on board) and have methods to extract certain information from current board state, such as checking for consecutive streaks. 
  
  <p align="center">
  <img src="https://i.ibb.co/6Rz1qDZ/Board-Terminal.png" />
</p>

  <p align="center">
  <i>GameBoard is printed in the terminal, and latest moved made on board is marked with an underdash '_'</i>
</p>
  
   
 * *BoardSubset:* 
 since the board can be of any size, it is computationally expensive to perform certain operations on the entirety of game board. In addition, since most Caro game usually focus on the center of the board, the corners and borders are mostly empty cells. Therefore, the BoardSubset class can be used to store the coordinates for a subset of the board, defined by the top and bottow row indexes, as well as the left-most and right-most column indexes. 
  
  
**4) Package streak:**

  * *Streak:* streak is defined as consecutive moves from the same player on game board. A streak of 5 (winning condition for Caro) is 5-in-a-row moves from a player. A streak is blocked on one side if it cannot be expand by playing a move of the same character on that side (either blocked by an opponent's move or by the bound of the game board). The Streak class stores count of unblocked and blocked-on-one-side streaks of a certain length. It does not store count of streaks that are blocked on both sides, since that streak cannot be expanded and is therefore useless in terms of gameplay. 
  
<p align="center">
  <img src="https://i.ibb.co/9NWS7PN/Blocked-versus-unblocked.png" />
</p>

<p align="center">
    <i><b>(Left)</b> Streak of length 3, unblocked;  <b>(Right)</b> Streak of length 4, blocked</i>
</p>
  
  * *StreakList:* is an array of Streak objects of different streak length, ranging from 2 to WIN_CONDITION, inclusive. The StreakList is compiled by checking the entire board for streaks from a player, and storing any valid streaks of length 2 and above. A StreakList object is then used to calculate the utility score of the board state for that player. 
  
**5) Package AI:** 

  * *MinimaxAI:* an AI for caro which makes decision based on depth-limited minimax algorithm. A minimax algorithm is an adversial search algorithm commonly used to make decisions in a 2-player game. 
  
  
  I learned to implement minimax algorithm within a Tic-Tac-Toe game during CSCI E-80, and decided to expand upon it by applying to Caro. For Caro, it is significantly more challenging to implement this algoritm. This is because it is more computationally expensive to apply the algorithm until the game is terminated by a winning move: 5-in-a-row is needed instead of 3-in-a-row, and the board size is larger. Therefore, a depth-limited minimax function that terminates after reaching a certain depth is more preferrable. 
  
 
  The utility scoring/estimating function utilizes heuristic/knowledge of the game to minimize the depth needed to make a quality decision. Utility scores range from 1 (AI player is winning) to -1 (opponent player is winning). A StreakList is compiled from a board state find the number of streaks of different sizes. The scoring function then takes this StreakList object and calculate a score. 
  
  Since the objective is to make 5-in-a-row, it is favorable to make streaks as long as possible, so streaks with longer length are scored higher than shorter lengths. Since an unblocked streak offers more potential for expanding, unblocked streak are scored higher than blocked ones with the same length. 
  
<p align="center">
  <img src="https://i.ibb.co/MSQ2BLV/Utility.png" />
</p>
  
  
  
  Certain board states are win-adjacent, in which no winning streak can be detected but streaks that will result in a win condition is detected. One such example is an unblocked streak-of-4, it is not 5-in-a-row, but there is no way an opponent can block both side to prevent this from expanding to 5-in-a-row. These win-adjacent streak are scored at 1.0 in the utility function. 


**4. How to use this project:**

Run ./test/GameTest.java to start the game. The command line will prompt users for the row and column of moves to be made, and then print out current board state. The command prompt will continue asking for moves until the entire board is filled or a player reaches win condition. 

Demo:

https://user-images.githubusercontent.com/87917284/198199327-1d3f9d22-934b-4f2a-aeb6-2743fb166db8.mp4

In this video, I am player O, and the AI is player X (starting player). The AI won.

**5. Current limitations**

* Game board size cannot be larger than 99 by 99. 
* Players need to input in the coordinate of moves to make, which can be a hassle and mistake-prone. A GUI will help make the game more playable by human player. 


**6. Future ideas:**

* Implement game variant in which a 5-in-a-row which is blocked on both side does not win the game.
* Implement a GUI for the game so players can click on positions on game board to make a move instead of having to input move's coordinates from command line.  
* Deploy this project on the web using Spring Boot so people can easily play the game against minimax AI. 
