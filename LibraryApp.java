/*
 * CSC 1102 Practical Exam Project - Library Management System
 * Group name:   GROUP 7
 * Group leader: Alan Twesigye
 * Session:      Day 
 * https://github.com/necodemasdeng-beep/LibraryManagementSystem
 */
// Import the AWT library: gives us layouts such as BorderLayout and GridLayout.
import java.awt.*;
// Import the Swing library: gives us the window and components (JFrame, JButton, JTextField...).
import javax.swing.*;

// LibraryApp is the main class. "extends JFrame" means this class IS a window (inheritance).
public class LibraryApp extends JFrame {
    // ARRAYS: these store many objects. No database is used, as the exam allows.
    private Book[] books = new Book[50];        // space for up to 50 Book objects
    private Reader[] readers = new Reader[50];  // space for up to 50 Reader objects
    // Counters: how many books/readers are stored so far (also the next free position).
    private int bookCount = 0, readerCount = 0;

    // GUI components that several methods need, so they are declared as attributes.
    private JTextField bId, bTitle, bAuthor, rId, rName;  // the five text boxes
    private JTextArea area;                               // the output area at the bottom

    // Constructor: runs once when the program starts and builds the whole window.
    public LibraryApp() {
        setTitle("GROUP 7: Library Management System");  // text shown in the window's title bar
        setSize(650, 650);                               // window size: 650 wide, 650 high
        setLayout(new BorderLayout());                   // split window into NORTH, CENTER, SOUTH...

        // ----- INPUT PANEL: labels and text boxes in a grid of 5 rows x 2 columns -----
        JPanel input = new JPanel(new GridLayout(5, 2, 5, 5));  // last two numbers = gaps between cells
        // Each line adds a label, creates its text box, then adds the text box beside the label.
        input.add(new JLabel("Book ID:")); bId = new JTextField(); input.add(bId);
        input.add(new JLabel("Title:")); bTitle = new JTextField(); input.add(bTitle);
        input.add(new JLabel("Author:")); bAuthor = new JTextField(); input.add(bAuthor);
        input.add(new JLabel("Reader ID:")); rId = new JTextField(); input.add(rId);
        input.add(new JLabel("Reader Name:")); rName = new JTextField(); input.add(rName);

        // ----- BUTTON PANEL: create the buttons the user clicks -----
        JPanel btns = new JPanel();                           // a panel to hold all the buttons
        JButton addBook = new JButton("Add Book");            // stores a new book
        JButton addReader = new JButton("Add Reader");        // registers a new reader
        JButton view = new JButton("View All Books");         // lists every book
        JButton viewAvail = new JButton("View Available");    // lists only books on the shelf
        JButton viewReaders = new JButton("View Readers");    // lists all registered readers
        JButton borrow = new JButton("Borrow");               // lends a book to a reader
        JButton ret = new JButton("Return");                  // takes a book back
        btns.add(addBook); btns.add(addReader); btns.add(view); btns.add(viewAvail);  // put buttons
        btns.add(viewReaders); btns.add(borrow); btns.add(ret);                       // on the panel

        // ----- OUTPUT AREA: where all messages and lists are displayed -----
        area = new JTextArea(10, 40);   // 10 rows, 40 columns of text
        area.setEditable(false);        // the user can read it but not type in it
        add(input, BorderLayout.NORTH);               // inputs at the top of the window
        add(btns, BorderLayout.CENTER);               // buttons in the middle
        add(new JScrollPane(area), BorderLayout.SOUTH); // output at the bottom, with a scroll bar

        // ADD BOOK button. The "e ->" is a lambda: code that runs when the button is clicked.
        addBook.addActionListener(e -> {
            String id = bId.getText().trim();          // read the Book ID box; trim() removes spaces
            String title = bTitle.getText().trim();    // read the Title box
            String author = bAuthor.getText().trim();  // read the Author box
            // IF / ELSE IF chain: check problems first, and only add the book if there are none.
            if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {
                area.setText("Fill Book ID, Title and Author");      // some box was left empty
            } else if (bookCount >= books.length) {
                area.setText("Library is full");                     // array has no free space left
            } else if (findBook(id) >= 0) {
                area.setText("Book ID already exists");              // ID is already used
            } else {
                books[bookCount++] = new Book(id, title, author);    // create a Book, store it, then bookCount + 1
                area.setText("Added: " + title);                     // confirm to the user
                bId.setText(""); bTitle.setText(""); bAuthor.setText("");  // clear the boxes
            }
        });

        // ADD READER button: same idea as Add Book, but for readers.
        addReader.addActionListener(e -> {
            String id = rId.getText().trim();      // read the Reader ID box
            String name = rName.getText().trim();  // read the Reader Name box
            if (id.isEmpty() || name.isEmpty()) {
                area.setText("Fill Reader ID and Reader Name");      // a box was left empty
            } else if (readerCount >= readers.length) {
                area.setText("Reader list is full");                 // array is full
            } else if (findReader(id) >= 0) {
                area.setText("Reader ID already exists");            // duplicate ID
            } else {
                readers[readerCount++] = new Reader(id, name);       // create a Reader and store it
                area.setText("Registered: " + name);                 // confirm to the user
                rId.setText(""); rName.setText("");                  // clear the boxes
            }
        });

        // VIEW ALL BOOKS button. Uses a FOR loop to go through every stored book.
        view.addActionListener(e -> {
            area.setText("--- All Books ---\n");                     // heading (\n = new line)
            for (int i = 0; i < bookCount; i++) {                    // i goes 0, 1, 2 ... up to the last book
                // Ternary (short if/else): if borrowed show who has it, otherwise show AVAILABLE.
                String s = books[i].isBorrowed() ? "BORROWED by " + books[i].getBorrowedBy() : "AVAILABLE";
                // append() adds a line to the end of the output area.
                area.append(books[i].getId() + " | " + books[i].getTitle() + " by " + books[i].getAuthor() + " [" + s + "]\n");
            }
            if (bookCount == 0) area.append("No books");              // nothing stored yet
        });

        // VIEW AVAILABLE button: a FOR loop with an IF inside it.
        viewAvail.addActionListener(e -> {
            area.setText("--- Available Books ---\n");                // heading
            int shown = 0;                                           // counts how many books we display
            for (int i = 0; i < bookCount; i++) {                    // go through every book
                if (!books[i].isBorrowed()) {                        // "!" = NOT: only books that are not borrowed
                    area.append(books[i].getId() + " | " + books[i].getTitle() + " by " + books[i].getAuthor() + "\n");
                    shown++;                                         // one more book displayed
                }
            }
            if (shown == 0) area.append("No available books");       // every book is borrowed (or none exist)
        });

        // VIEW READERS button: a FOR loop that lists every registered reader.
        viewReaders.addActionListener(e -> {
            area.setText("--- Readers ---\n");                       // heading
            for (int i = 0; i < readerCount; i++) {                  // go through every reader
                area.append(readers[i].getId() + " | " + readers[i].getName() + "\n");
            }
            if (readerCount == 0) area.append("No readers");         // nobody registered yet
        });

        // BORROW button. First work out what happened (a code), then SWITCH shows the matching message.
        borrow.addActionListener(e -> {
            int b = findBook(bId.getText().trim());       // position of the book in the array, or -1
            int r = findReader(rId.getText().trim());     // position of the reader in the array, or -1
            int code;                                     // 0 = success, 1/2/3 = different problems
            if (b < 0) code = 1;                          // book not found
            else if (r < 0) code = 2;                     // reader not registered
            else if (books[b].isBorrowed()) code = 3;     // someone already has this book
            else code = 0;                                // everything is fine

            switch (code) {                               // SWITCH chooses what to do for each code
                case 0:
                    books[b].setBorrowed(true);                          // mark the book as borrowed
                    books[b].setBorrowedBy(readers[r].getId());          // link the book to this reader
                    area.setText(readers[r].getName() + " borrowed: " + books[b].getTitle());
                    break;                                               // stop; do not run the next cases
                case 1: area.setText("Book not found. Type the Book ID."); break;
                case 2: area.setText("Reader not registered. Type the Reader ID."); break;
                case 3: area.setText("Already borrowed by " + books[b].getBorrowedBy()); break;
            }
        });

        // RETURN button: makes a borrowed book available again.
        ret.addActionListener(e -> {
            int b = findBook(bId.getText().trim());       // find the book the user typed
            if (b < 0) {
                area.setText("Book not found. Type the Book ID.");   // no such book
            } else if (!books[b].isBorrowed()) {
                area.setText("This book was not borrowed");          // cannot return a book that is on the shelf
            } else {
                books[b].setBorrowed(false);                         // book is available again
                books[b].setBorrowedBy("");                          // clear the borrower
                area.setText("Returned: " + books[b].getTitle());    // confirm to the user
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);  // clicking the X closes the whole program
        setLocationRelativeTo(null);              // show the window in the centre of the screen
        setVisible(true);                         // make the window appear
    }

    // Search with a WHILE loop. Returns the position of the book, or -1 if not found.
    // (private: only this class uses it, as a helper method)
    private int findBook(String id) {
        int i = 0;                                        // start at the first book
        while (i < bookCount) {                           // keep going while there are books left to check
            if (books[i].getId().equalsIgnoreCase(id)) {  // same ID? (ignores capital/small letters)
                return i;                                 // found it: return its position
            }
            i++;                                          // move to the next book
        }
        return -1;                                        // checked all books and found none
    }

    // Search with a DO-WHILE loop. Returns the position of the reader, or -1 if not found.
    private int findReader(String id) {
        if (readerCount == 0) return -1;   // do-while always runs once first, so stop early if there are no readers
        int i = 0;                         // start at the first reader
        do {
            if (readers[i].getId().equalsIgnoreCase(id)) {  // same ID? (ignores case)
                return i;                                   // found: return the position
            }
            i++;                                            // move to the next reader
        } while (i < readerCount);         // repeat while there are readers left (condition checked AFTER the body)
        return -1;                         // not found
    }

    // main: where the program starts running.
    public static void main(String[] args) {
        // invokeLater runs the window-creating code safely on Swing's own thread.
        SwingUtilities.invokeLater(() -> new LibraryApp());  // create a LibraryApp object = open the window
    }
}
