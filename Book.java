// The Book class is a blueprint (template) for ONE book in the library.
// Every book we add to the system is an object created from this class.
public class Book {
    // Attributes (data each book stores). They are private so that other classes
    // cannot change them directly; they must use the getters and setters below.
    // This is called encapsulation.
    private String id;          // unique code of the book, e.g. B001
    private String title;       // name of the book
    private String author;      // person who wrote the book
    private boolean borrowed;   // true = someone has the book, false = it is on the shelf
    private String borrowedBy;  // ID of the reader who has the book (empty if nobody)

    // Constructor: runs automatically when we write new Book(id, title, author).
    // It sets the starting values of a new book.
    public Book(String id, String title, String author) {
        this.id = id;           // "this.id" is the attribute; "id" is the value passed in
        this.title = title;     // store the title that was passed in
        this.author = author;   // store the author that was passed in
        this.borrowed = false;  // a new book always starts as available
        this.borrowedBy = "";   // and nobody has borrowed it yet
    }

    // Getters: methods that let other classes READ the private attributes.
    public String getId() { return id; }                    // returns the book ID
    public String getTitle() { return title; }              // returns the title
    public String getAuthor() { return author; }            // returns the author
    public boolean isBorrowed() { return borrowed; }        // returns true if borrowed
    public String getBorrowedBy() { return borrowedBy; }    // returns the borrower's ID

    // Setters: methods that let other classes CHANGE the borrowing information.
    public void setBorrowed(boolean b) { borrowed = b; }                // mark borrowed / available
    public void setBorrowedBy(String readerId) { borrowedBy = readerId; } // remember who has it
}
