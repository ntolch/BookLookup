import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

class ValidateBookResultsTests {
	BookSearch mockBookSearch = Mockito.mock(BookSearch.class);

	public static ArrayList<Book> searchResults = new ArrayList<Book>();
	public static List<Book> topFive;
	public static ArrayList<Book> readingList = new ArrayList<Book>();
	public static ArrayList<String> authors = new ArrayList<String>();
	
	public static Book book1;
	public static Book book2;
	public static Book book3;
	public static Book book4;
	public static Book book5;
	public static Book book6;

	@BeforeEach
	public void setUp() {
		book1 = new Book("title1", authors, "publisher1");
		book2 = new Book("title2", authors, "publisher2");
		book3 = new Book("title3", authors, "publisher3");
		book4 = new Book("title4", authors, "publisher4");
		book5 = new Book("title5", authors, "publisher5");
		book6 = new Book("title6", authors, "publisher6");
		
		searchResults.add(book1);
		searchResults.add(book2);
		searchResults.add(book3);
		searchResults.add(book4);
		searchResults.add(book5);
		searchResults.add(book6);
	}
	
	@Test
	public void returnFiveBooksFromSearchResults() {
		authors.add("John Doe");
		authors.add("Bill Gates");
		
		topFive = mockBookSearch.returnFiveBooks(searchResults);
				
		assertEquals(topFive.size(), 5);
	}
	
	@Test
	public void returnedBooksAreValidBooks() {
		topFive = mockBookSearch.returnFiveBooks(searchResults);
		
		assertThat(topFive.get(0), instanceOf(Book.class));
	}

}
