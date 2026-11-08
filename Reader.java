// The Reader class is a blueprint for ONE registered library member.
// Each reader we register is an object created from this class.
public class Reader {
    // Attributes: private, so they can only be accessed through the getters (encapsulation).
    private String id;      // unique code of the reader, e.g. R001
    private String name;    // the reader's name

    // Constructor: runs when we write new Reader(id, name) and sets the starting values.
    public Reader(String id, String name) {
        this.id = id;       // store the ID that was passed in
        this.name = name;   // store the name that was passed in
    }

    // Getters: let other classes read the private attributes.
    // There are no setters because a reader's ID and name do not change in our program.
    public String getId() { return id; }       // returns the reader ID
    public String getName() { return name; }   // returns the reader's name
}
