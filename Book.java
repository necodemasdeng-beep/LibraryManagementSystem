public class Book {
    private String id;
    private String title;
    private String author;
    private boolean borrowed;
    private String borrowedBy;   // FIX: remembers WHICH reader has the book

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.borrowed = false;
        this.borrowedBy = "";
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public boolean isBorrowed() { return borrowed; }
    public String getBorrowedBy() { return borrowedBy; }
    public void setBorrowed(boolean b) { borrowed = b; }
    public void setBorrowedBy(String readerId) { borrowedBy = readerId; }
}
