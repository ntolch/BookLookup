import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BookSearch {
	static String key = "&key=AIzaSyALRg95_bGfi-bWtfNINh07jzxD3NU0xz4";
	static String searchTerms = "";

	static ArrayList<Book> searchResults = new ArrayList<Book>();
	static List<Book> topFive;
	static ArrayList<Book> readingList = new ArrayList<Book>();

	static Boolean bookSearched = false;

	public static void main(String[] args) {
		// If book search has just been conducted, offer to add to Reading List
		if (bookSearched) {
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getMoreActions();
			bookSearched = false;
		}
		getAction();
	}

	public static void getKeywordFromUser() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nEnter a keyword:");

		while (scanner.hasNextLine()) {
			searchTerms = scanner.nextLine().trim();
			if (searchTerms.isEmpty()) {
				System.out.println("\nPlease enter a keyword:");
				scanner = new Scanner(System.in);
			} else {
				System.out.println("\nSearching for books with keyword '" + searchTerms + "'");

				searchTerms = searchTerms.replace(" ", "-");
				System.out.println("...\n");

				try {
					bookSearch(searchTerms);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void printReadingList() {
		if (readingList.size() > 0) {
			System.out.println("Your Reading List\n");
			int i = 1;
			for (Book book : readingList) {
				System.out.println("Book " + i + ":");
				book.printBook();
				System.out.println("");
				i++;
			}
		} else {
			try {
				Thread.sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("\nYour Reading List is empty.");
		}
		System.out.println("");
		try {
			Thread.sleep(1800);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		getAction();
	}

	public static void getAction() {
		System.out.println("What would you like to do?");
		System.out.println("1. Search for books");
		System.out.println("2. View my Reading List");
		System.out.println("(Type 1 or 2)");

		Scanner scanner = new Scanner(System.in);

		Boolean gotValidAnswer = false;
		while (!gotValidAnswer) {
			while (!scanner.hasNextInt()) {
				System.out.println("\nI don't understand...");
				System.out.println("Please type '1' or '2'");
				scanner = new Scanner(System.in);
			}

			while (scanner.hasNextInt()) {
				int action = scanner.nextInt();                
				if (action == 1) {
					gotValidAnswer = true;
					getKeywordFromUser();
				} else if (action == 2) {
					gotValidAnswer = true;
					printReadingList();
				} else {
					System.out.println("\nPlease type '1' or '2'");
				}
			}
		}
	}

	public static void getMoreActions() {
		System.out.println("Would you like to add one of these books to your Reading List?");
		System.out.println("(Type \'yes\' or \'no\')");
		Scanner scanner = new Scanner(System.in);

		Boolean gotValidAnswer = false;
		while (!gotValidAnswer) {
			while (scanner.hasNextLine()) {
				String answer = scanner.nextLine().toLowerCase().trim();
				if (answer.startsWith("y")) {
					gotValidAnswer = true;
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					addToReadingList();
				} else if (answer.startsWith("n")) {
					gotValidAnswer = true;
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					getAction();
				} else {
					System.out.println("Sorry, was that a yes or a no?");
				}
			}
		}   
	}

	public static void addToReadingList() {
		Scanner scanner = new Scanner(System.in);
		System.out.println("\nSelect a book (1-5) to add it to your Reading List.");

		Boolean validBook = false;
		while (!validBook) {
			while (!scanner.hasNextInt()) {
				System.out.println("That...doesn\'t look valid.");
				System.out.println("Please choose a book by typing 1, 2, 3, 4, or 5.");
				scanner = new Scanner(System.in);
			}
			while (scanner.hasNextInt()) {
				int selectedBookInt = scanner.nextInt();

				if (selectedBookInt > 0 && selectedBookInt < 6) { 
					validBook = true; 
					Book selectedBook = topFive.get(selectedBookInt - 1);
					readingList.add(selectedBook);

					try {
						Thread.sleep(1500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.print(selectedBook.getTitle() + " has been added to your Reading List.\n\n");

					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					getAction();
				} else {
					System.out.println("That...doesn\'t look valid.");
					System.out.println("Please choose a book by typing 1, 2, 3, 4, or 5.");
				}
			}
		}
	}

	public static void printFiveResults() {
		if (topFive != null && topFive.size() > 0) {
			System.out.println("Your Search Results\n");
			int i = 1;
			for (Book book : topFive) {
				System.out.println(i);
				book.printBook();
				System.out.println("");
				i++;
			}
		} else {
			System.out.println("/nHm...Try another search.");
			getKeywordFromUser();
		}
	}


	public static List<Book> returnFiveBooks(ArrayList<Book> searchResults) {
		// Select 5 books at random from search results
		Collections.shuffle(searchResults);
		topFive = searchResults.subList(0, 5);

		printFiveResults();

		return topFive;
	}

	public static void bookSearch(String searchTerms) throws Exception {
		String result = "";
		searchResults.clear();
		String urlString = "https://www.googleapis.com/books/v1/volumes?q="
				+ searchTerms + key;

		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setRequestMethod("GET");

		InputStream inputStream = urlConnection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputStream);
		int data = reader.read();

		while (data != -1) {
			char current = (char) data;
			result += current;
			data = reader.read();
		}

		//Read JSON response
		JSONObject jsonObject = new JSONObject(result.toString());
		JSONArray jsonArray = jsonObject.getJSONArray("items");

		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject currentItem = jsonArray.getJSONObject(i);
			JSONObject info = currentItem.getJSONObject("volumeInfo");

			String title = info.optString("title");
			String publisher = info.optString("publisher", "unknown");

			ArrayList<String> authorList = new ArrayList<String>();

			// If an author is listed, retrieve them; otherwise, default to "Unknown"
			if (info.has("authors")) {
				JSONArray authorJSONArray = info.getJSONArray("authors");
				// GET request returns authors as an array
				// Add each author to list of book's authors
				for (int j = 0; j < authorJSONArray.length(); j++) {
					authorList.add(authorJSONArray.getString(j));
				}
			} else {
				authorList.add("unknown");
			}
			searchResults.add(new Book(title, authorList, publisher));
		}

		returnFiveBooks(searchResults);

		bookSearched = true;

		try {
			Thread.sleep(800);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		main(null);
	}
}
