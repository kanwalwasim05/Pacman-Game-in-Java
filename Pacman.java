import java.util.*; // Importing utility package for Scanner and Random classes

//POSITION CLASS
class Position {

    // Variables to store coordinates
    int x, y;

    // Constructor of Position class
    // Used to initialize x and y coordinates
    Position(int x, int y) {

        this.x = x; // Assign x coordinate
        this.y = y; // Assign y coordinate
    }
}

//ABSTRACT WALKER CLASS
abstract class Walker {

    // Every walker has a position(Composition)
    Position pos;

    // Constructor of Walker class
    Walker(int x, int y) {

        // Create Position object
        pos = new Position(x, y);
    }

    // Abstract method
    abstract void move(char[][] maze);
}

//RANDOM WALKER CLASS (Ghost)
class RandomWalker extends Walker {

    // Random object for random movement
    Random rand = new Random();

    // Constructor of RandomWalker
    RandomWalker(int x, int y) {

        // Calling parent constructor
        super(x, y);
    }

    // Ghost movement method
    // Method overriding
    void move(char[][] maze) {

        // Movement arrays
        // dx controls row movement
        int[] dx = {-1,1,0,0};

        // dy controls column movement
        int[] dy = {0,0,-1,1};

        // Generate random direction from 0 to 3
        int dir = rand.nextInt(4);

        int nx = pos.x + dx[dir];

        int ny = pos.y + dy[dir];

        if (maze[nx][ny] != '#') {

            // Update ghost position
            pos.x = nx;
            pos.y = ny;
        }
    }
}

//DIRECTED WALKER CLASS (Pacman)
class DirectedWalker extends Walker {

    // Constructor of DirectedWalker
    DirectedWalker(int x, int y) {

        // Calling Walker constructor
        super(x, y);
    }

    // Pacman movement method
    // Takes maze and user input
    void move(char[][] maze, char input) {

        // Store current coordinates
        int nx = pos.x;
        int ny = pos.y;

        // W = Move Up
        if (input=='W')
            nx--;

            // S = Move Down
        else if (input=='S')
            nx++;

            // A = Move Left
        else if (input=='A')
            ny--;

            // D = Move Right
        else if (input=='D')
            ny++;

        // If next position is not wall
        if (maze[nx][ny] != '#') {

            // Update Pacman coordinates
            pos.x = nx;
            pos.y = ny;
        }
    }

    // Method to print game title
    void printTitle() {

        System.out.println("\n");

        System.out.println("🟨🟨🟨   🟨🟨🟨   🟨🟨🟨🟨 🟨         🟨    🟨🟨     🟨      🟨  ");
        System.out.println("🟨   🟨  🟨   🟨  🟨        🟨 🟨   🟨 🟨  🟨    🟨  🟨 🟨    🟨  ");
        System.out.println("🟨🟨🟨   🟨🟨🟨  🟨        🟨    🟨    🟨 🟨🟨🟨🟨  🟨   🟨  🟨   ");
        System.out.println("🟨       🟨   🟨  🟨        🟨          🟨 🟨     🟨  🟨    🟨 🟨   ");
        System.out.println("🟨       🟨   🟨  🟨🟨🟨🟨 🟨          🟨 🟨     🟨  🟨       🟨    ");
        System.out.println("                                                                        ");
    }

    // Empty overridden method
    // Required because Walker has abstract move method
    void move(char[][] maze) {}
}

// ---------------- WORLD CLASS ----------------
class World {

    // 2D maze array
    // # = Wall
    // . = Dot
    // H = Hurdle
    // C = Cherry bonus
    char[][] maze = {

            {'#','#','#','#','#','#','#','#','#','#'},
            {'#','.','.','.','H','.','.','.','C','#'},
            {'#','.','#','.','#','.','#','.','.','#'},
            {'#','.','#','.','.','.','#','.','.','#'},
            {'#','.','.','#','#','.','.','.','.','#'},
            {'#','.','.','.','.','.','#','.','.','#'},
            {'#','.','#','#','.','#','#','.','.','#'},
            {'#','.','.','.','.','.','.','.','.','#'},
            {'#','C','.','#','.','#','.','H','.','#'},
            {'#','#','#','#','#','#','#','#','#','#'}
    };

    // Creating Pacman object
    DirectedWalker pacman = new DirectedWalker(1,1);

    // Creating array of Ghost objects
    RandomWalker[] ghosts = {

            new RandomWalker(8,8),
            new RandomWalker(1,8),
            new RandomWalker(5,5)
    };

    // Score variable
    int score = 0;

    // Player lives
    int lives = 3;

    // Count total dots in maze
    int dots = countDots();

    // Hurdle hit counter
    int hurdleHits = 0;

    // Scanner object for input
    Scanner sc = new Scanner(System.in);

    // Method to count dots in maze
    int countDots() {

        // Counter variable
        int c = 0;

        // Loop through every row
        for (char[] row : maze)

            // Loop through every character
            for (char ch : row)

                // If dot found
                if (ch == '.')
                    c++;

        // Return total dots
        return c;
    }

    // Method to print board
    void printBoard() {

        // Temporary maze array
        char[][] temp = new char[maze.length][maze[0].length];

        // Copy original maze into temp
        for (int i=0;i<maze.length;i++)
            temp[i] = maze[i].clone();

        // Place ghosts in temp maze
        for (RandomWalker g : ghosts)
            temp[g.pos.x][g.pos.y] = 'G';

        // Place Pacman in temp maze
        temp[pacman.pos.x][pacman.pos.y] = 'P';

        // Print maze row by row
        for (char[] row : temp) {

            // Print every character
            for (char c : row) {

                // Switch statement for symbols
                switch (c) {

                    case '#':
                        System.out.print("█ ");
                        break;

                    case '.':
                        System.out.print("· ");
                        break;

                    case 'P':
                        System.out.print("😃 ");
                        break;

                    case 'G':
                        System.out.print("👻 ");
                        break;

                    case 'C':
                        System.out.print("🍒 ");
                        break;

                    case 'H':
                        System.out.print("🚧 ");
                        break;

                    default:
                        System.out.print("  ");
                }
            }

            System.out.println();
        }

        // Print score and lives
        System.out.println("\nScore: " + score + " | Lives: " + hearts());
    }

    // Method to generate hearts
    String hearts() {

        String h = "";

        // Add heart according to lives
        for (int i=0;i<lives;i++)
            h += "❤️ ";

        return h;
    }

    // Method for hurdle
    void hurdle() {

        // Increase hurdle hits
        hurdleHits++;

        System.out.println("Hurdle hit!🚩 (" + hurdleHits + "/3)");

        pause();

        if (hurdleHits == 3) {

            lives--;

            // Reset hurdle counter
            hurdleHits = 0;

            System.out.println("💔 Too many hurdles! You lost a life!");

            pause();

            if (lives == 0) {

                System.out.println("GAME OVER");

                System.exit(0);
            }
        }
    }

    // Method to pause game
    void pause() {

        try {

            // Pause for 700 milliseconds
            Thread.sleep(700);

        } catch(Exception e){}
    }

    // Main game loop method
    void play() {

        // Print title
        pacman.printTitle();

        pause();

        // Infinite loop
        while (true) {

            // Print game board
            printBoard();

            // Ask user for movement
            System.out.print("Move (W/A/S/D/Q): ");

            // Take user input
            char move = sc.next().toUpperCase().charAt(0);

            // Quit game if Q is pressed
            if (move == 'Q') {

                System.out.println("GAME QUIT");

                return;
            }

            // Move Pacman
            pacman.move(maze, move);

            // Store current cell
            char cell = maze[pacman.pos.x][pacman.pos.y];

            // If Pacman eats dot
            if (cell == '.') {

                // Increase score
                score++;

                // Decrease total dots
                dots--;

                // Remove dot
                maze[pacman.pos.x][pacman.pos.y] = ' ';
            }

            // If Pacman gets cherry
            else if (cell == 'C') {

                // Bonus score
                score += 10;

                // Remove cherry
                maze[pacman.pos.x][pacman.pos.y] = ' ';

                System.out.println("🍒 BONUS!");

                pause();
            }

            // If Pacman hits hurdle
            else if (cell == 'H') {

                hurdle();
            }

            // Move all ghosts
            for (RandomWalker g : ghosts)
                g.move(maze);

            // Check collision with ghosts
            for (RandomWalker g : ghosts) {

                // If ghost catches Pacman
                if (g.pos.x == pacman.pos.x && g.pos.y == pacman.pos.y) {

                    lives--;

                    System.out.println("💀 Ghost caught you!");

                    pause();

                    // Reset Pacman position
                    pacman.pos = new Position(1,1);

                    // If no lives left
                    if (lives == 0) {

                        System.out.println("GAME OVER");

                        return;
                    }
                }
            }

            // If all dots eaten
            if (dots == 0) {

                System.out.println("🎉 YOU WIN!");

                return;
            }
        }
    }
}

//MAIN CLASS / DRIVER PROGRAM
public class Pacman {

    public static void main(String[] args) {

        // Create World object and start game
        new World().play();
    }
}