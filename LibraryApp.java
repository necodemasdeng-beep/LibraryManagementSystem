/*
 * CSC 1102 Practical Exam Project - Library Management System
 * Group name:   GROUP 7
 * Group leader: Alan Twesigye
 * Session:      Day 
 */
import java.awt.*;
import javax.swing.*;

public class LibraryApp extends JFrame {
    // Arrays - No Database as per PDF
    private Book[] books = new Book[50];
    private Reader[] readers = new Reader[50];
    private int bookCount = 0, readerCount = 0;

    private JTextField bId, bTitle, bAuthor, rId, rName;
    private JTextArea area;

    public LibraryApp() {
        setTitle("GROUP 7: Library Management System");
        setSize(650, 650);
        setLayout(new BorderLayout());

        JPanel input = new JPanel(new GridLayout(5, 2, 5, 5));
        input.add(new JLabel("Book ID:")); bId = new JTextField(); input.add(bId);
        input.add(new JLabel("Title:")); bTitle = new JTextField(); input.add(bTitle);
        input.add(new JLabel("Author:")); bAuthor = new JTextField(); input.add(bAuthor);
        input.add(new JLabel("Reader ID:")); rId = new JTextField(); input.add(rId);
        input.add(new JLabel("Reader Name:")); rName = new JTextField(); input.add(rName);

        JPanel btns = new JPanel();
        JButton addBook = new JButton("Add Book");
        JButton addReader = new JButton("Add Reader");
        JButton view = new JButton("View All Books");
        JButton viewAvail = new JButton("View Available");   // FIX: required by the exam example
        JButton viewReaders = new JButton("View Readers");   // FIX: readers can now be seen
        JButton borrow = new JButton("Borrow");
        JButton ret = new JButton("Return");
        btns.add(addBook); btns.add(addReader); btns.add(view); btns.add(viewAvail);
        btns.add(viewReaders); btns.add(borrow); btns.add(ret);

        area = new JTextArea(10, 40);
        area.setEditable(false);
        add(input, BorderLayout.NORTH);
        add(btns, BorderLayout.CENTER);
        add(new JScrollPane(area), BorderLayout.SOUTH);

        // ADD BOOK - if/else
        addBook.addActionListener(e -> {
            String id = bId.getText().trim();
            String title = bTitle.getText().trim();
            String author = bAuthor.getText().trim();
            if (id.isEmpty() || title.isEmpty() || author.isEmpty()) {   // FIX: author checked too
                area.setText("Fill Book ID, Title and Author");
            } else if (bookCount >= books.length) {                      // FIX: no more crash when full
                area.setText("Library is full");
            } else if (findBook(id) >= 0) {                              // FIX: no duplicate IDs
                area.setText("Book ID already exists");
            } else {
                books[bookCount++] = new Book(id, title, author);
                area.setText("Added: " + title);
                bId.setText(""); bTitle.setText(""); bAuthor.setText("");
            }
        });

        // ADD READER - FIX: now has the same checks as Add Book
        addReader.addActionListener(e -> {
            String id = rId.getText().trim();
            String name = rName.getText().trim();
            if (id.isEmpty() || name.isEmpty()) {
                area.setText("Fill Reader ID and Reader Name");
            } else if (readerCount >= readers.length) {
                area.setText("Reader list is full");
            } else if (findReader(id) >= 0) {
                area.setText("Reader ID already exists");
            } else {
                readers[readerCount++] = new Reader(id, name);
                area.setText("Registered: " + name);
                rId.setText(""); rName.setText("");
            }
        });

        // VIEW ALL BOOKS - for loop
        view.addActionListener(e -> {
            area.setText("--- All Books ---\n");
            for (int i = 0; i < bookCount; i++) {
                String s = books[i].isBorrowed() ? "BORROWED by " + books[i].getBorrowedBy() : "AVAILABLE";
                area.append(books[i].getId() + " | " + books[i].getTitle() + " by " + books[i].getAuthor() + " [" + s + "]\n");
            }
            if (bookCount == 0) area.append("No books");
        });

        // VIEW AVAILABLE - for loop with if
        viewAvail.addActionListener(e -> {
            area.setText("--- Available Books ---\n");
            int shown = 0;
            for (int i = 0; i < bookCount; i++) {
                if (!books[i].isBorrowed()) {
                    area.append(books[i].getId() + " | " + books[i].getTitle() + " by " + books[i].getAuthor() + "\n");
                    shown++;
                }
            }
            if (shown == 0) area.append("No available books");
        });

        // VIEW READERS - for loop
        viewReaders.addActionListener(e -> {
            area.setText("--- Readers ---\n");
            for (int i = 0; i < readerCount; i++) {
                area.append(readers[i].getId() + " | " + readers[i].getName() + "\n");
            }
            if (readerCount == 0) area.append("No readers");
        });

        // BORROW - FIX: needs a registered reader; switch picks the message
        borrow.addActionListener(e -> {
            int b = findBook(bId.getText().trim());
            int r = findReader(rId.getText().trim());
            int code;
            if (b < 0) code = 1;                      // book not found
            else if (r < 0) code = 2;                 // reader not registered
            else if (books[b].isBorrowed()) code = 3; // already taken
            else code = 0;                            // OK

            switch (code) {
                case 0:
                    books[b].setBorrowed(true);
                    books[b].setBorrowedBy(readers[r].getId());
                    area.setText(readers[r].getName() + " borrowed: " + books[b].getTitle());
                    break;
                case 1: area.setText("Book not found. Type the Book ID."); break;
                case 2: area.setText("Reader not registered. Type the Reader ID."); break;
                case 3: area.setText("Already borrowed by " + books[b].getBorrowedBy()); break;
            }
        });

        // RETURN - FIX: checks the book was really borrowed
        ret.addActionListener(e -> {
            int b = findBook(bId.getText().trim());
            if (b < 0) {
                area.setText("Book not found. Type the Book ID.");
            } else if (!books[b].isBorrowed()) {
                area.setText("This book was not borrowed");
            } else {
                books[b].setBorrowed(false);
                books[b].setBorrowedBy("");
                area.setText("Returned: " + books[b].getTitle());
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Search with a WHILE loop. Returns the position of the book, or -1 if not found.
    private int findBook(String id) {
        int i = 0;
        while (i < bookCount) {
            if (books[i].getId().equalsIgnoreCase(id)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    // Search with a DO-WHILE loop. Returns the position of the reader, or -1 if not found.
    private int findReader(String id) {
        if (readerCount == 0) return -1;   // do-while runs once first, so check for empty
        int i = 0;
        do {
            if (readers[i].getId().equalsIgnoreCase(id)) {
                return i;
            }
            i++;
        } while (i < readerCount);
        return -1;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryApp());
    }
}
