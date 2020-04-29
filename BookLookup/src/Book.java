import java.util.ArrayList;

public class Book {
    public String title;
    public ArrayList<String> authors;
    public String publisher;
            
    public Book(String title, ArrayList<String> authors, String publisher) {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public ArrayList<String> getAuthors() {
    	return authors;
    }
    
    public String getPublisher() {
    	return publisher;
    }
    
    public void printBook() {
    	System.out.println("Title: " + title);
    	
    	// If there's 1 author, print singular 'Author', otherwise print plural 'Authors'
    	if (authors.size() == 1) { 
    		System.out.println("Author: " + authors.get(0)); 
    	} else if (authors.size() > 1) {
    		// Join array of authors together in a single String
    		StringBuilder strBuilder = new StringBuilder("");
    		for (String author : authors) { strBuilder.append(author).append(", "); }
    		String stringOfAuthors = strBuilder.toString();
    		if (stringOfAuthors.length() > 0) {
    			stringOfAuthors = stringOfAuthors.substring(0, stringOfAuthors.length() - 1);
    		}
    		System.out.println("Authors: " + stringOfAuthors); 
    	}
		
    	System.out.println("Publisher: " + publisher);
    }
}