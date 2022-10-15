# Caro-Game

Thuy Trang Ngo

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



**3. Project Structure:**
* Game: implement the game play for Caro, including setting up players and gameboard, as well as simulating game play between players, checking for win condition, and terminating the game. 
* Player: represent a player in the game, stores a char that represents the player on board. 
* board package:
  * GameBoard: represent the game board as a 2D matrix of characters (representing moves made on board) and have methods to extract certain information from current board state, such as checking for consecutive streaks. 
  * BoardSubset: since the board can be of any size (ranging from 5 to 99, inclusive), it is computationally expensive to perform certain operations, such as checking board for streaks, on the entirety of game board. In addition, since most Caro game usually focus on the center of the board, the corners and borders are mostly empty cells. Therefore, the BoardSubset class can be used to store the coordinates for a subset of the board, defined by the top and bottow row indexes, as well as the left-most and right-most column indexes. It also contains method to calculate the indexes that represent a board subset, given a center point and a radius. 
  
  ![alt text](https://i.ibb.co/6Rz1qDZ/Board-Terminal.png)
  
  GameBoard is printed in the terminal, and latest moved made on board is marked with an underdash (_). 
  
* streak package:

  * Streak: streak is defined as consecutive moves from the same player on game board. A streak of 5 (winning condition for Caro) is 5-in-a-row moves from a player. A streak is blocked on one side if it cannot be expand by playing a move of the same character on that side (either blocked by an opponent's move or by the bound of the game board). The Streak class stores count of unblocked and blocked-on-one-side streaks of a certain length. It does not store count of streaks that are blocked on both sides, since that streak cannot be expanded and is therefore useless in terms of gameplay. 
  
    ![alt text](https://i.ibb.co/9NWS7PN/Blocked-versus-unblocked.png)
    
  **(Left)** Streak of length 3, unblocked;  **(Right)** Streak of length 4, blocked
  
  
  * StreakList: is an array of Streak objects of different streak length, ranging from 2 to WIN_CONDITION, inclusive. The StreakList is compiled by checking the entire board for streaks from a player, and storing any valid streaks of length 2 and above. A StreakList object is then used to calculate the utility score of the board state for that player. 
* AI package: 
  * MinimaxAI: an AI for caro which makes decision based on depth-limited minimax algorithm. A minimax algorithm is an adversial search commonly used to make decisions in a 2-player game. 
  
  I learned to implement this algorithm within a Tic-Tac-Toe game during CSCI E-80, and decided to expand upon it by applying to Caro. For Caro, it is significantly more challenging to implement this algoritm. This is because it is extremely computationally expensive to apply the algorithm until the game is terminated by a winning move, since 5-in-a-row is needed instead of 3-in-a-row (in Tic Tac Toe), and the board size is much larger (usually 15-by-15 instead of 3-by-3 in Tic Tac Toe). Therefore, in this project, I implement a depth-limited minimax function that terminates and calculate a utility score after a certain depth is reached. 
  
  The utility scoring/estimating function is more complex and utilizes heuristic/knowledge of the game (from my personal experience playing it) to minimize depth needed to make a quality decision. Utility scores range from 1 (AI player is winning) to -1 (opponent player is winning). A StreakList is compiled from a board state find the number of streaks of different sizes. The scoring function then takes this StreakList object and calculate a score. Since the objective is to make 5-in-a-row, it is favorable to make streaks as long as possible, so streaks with longer length are scored higher than shorter lengths. Since an unblocked streak offers more potential for expanding, unblocked streak are scored higher than blocked ones with the same length. In addition, certain board states are win-adjacent, in which no winning streak can be detected but streaks that will result in a win condition is detected. One such example is an unblocked streak-of-4, it is not 5-in-a-row, but there is no way an opponent can block both side to prevent this from expanding to 5-in-a-row. THese win-adjacent streak are scored at 1.0 in the utlity function. 


**4. How to use this project:**

Run ./test/GameTest.java to start the game. The command line will prompt users for the row and column of moves to be made, and then print out current board state. The command prompt will continue asking for moves until the entire board is filled or a player reaches win condition. 

**5. Current limitations**

* Game board size cannot be larger than 99 by 99. 
* Players need to input in the coordinate of moves to make, which can be a hassle and mistake-prone. A GUI will help make the game more playable by human player. 


**6. Future ideas:**

* Create an AI to play the game using mini-max algorithms or reinforcement learning.
* Implement game variant in which a 5-in-a-row which is blocked on both side does not win the game.
* Implement a GUI for the game so players can click on positions on game board to make a move instead of having to input move's coordinates from command line.  
